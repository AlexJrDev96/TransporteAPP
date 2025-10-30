package com.example.fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fragmentos.databinding.FragmentEscolaBinding
import com.example.fragmentos.db.entity.Escola
import com.example.fragmentos.ui.escola.EscolaListAdapter
import com.example.fragmentos.ui.escola.EscolaViewModel
import com.example.fragmentos.ui.escola.EscolaViewModelFactory

class EscolaFragment : Fragment() {

    private var _binding: FragmentEscolaBinding? = null
    private val binding get() = _binding!!

    private val escolaViewModel: EscolaViewModel by viewModels {
        EscolaViewModelFactory((activity?.application as TransporteApplication).escolaRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEscolaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = EscolaListAdapter()
        binding.recyclerViewEscolas.adapter = adapter
        binding.recyclerViewEscolas.layoutManager = LinearLayoutManager(context)

        escolaViewModel.allEscolas.asLiveData().observe(viewLifecycleOwner) {
            escolas ->
                escolas?.let { adapter.submitList(it) }
        }

        binding.buttonSalvarEscola.setOnClickListener {
            val nome = binding.editTextNomeEscola.text.toString()
            val endereco = binding.editTextEnderecoEscola.text.toString()
            val telefone = binding.editTextTelefoneEscola.text.toString()

            if (nome.isNotBlank()) {
                val escola = Escola(nome = nome, endereco = endereco, telefone = telefone)
                escolaViewModel.insert(escola)

                // Limpar campos após inserção
                binding.editTextNomeEscola.text.clear()
                binding.editTextEnderecoEscola.text.clear()
                binding.editTextTelefoneEscola.text.clear()
            } else {
                Toast.makeText(context, "Nome da escola é obrigatório", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
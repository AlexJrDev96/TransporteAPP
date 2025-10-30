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
import com.example.fragmentos.databinding.FragmentResponsavelBinding
import com.example.fragmentos.db.entity.Responsavel
import com.example.fragmentos.ui.responsavel.ResponsavelListAdapter
import com.example.fragmentos.ui.responsavel.ResponsavelViewModel
import com.example.fragmentos.ui.responsavel.ResponsavelViewModelFactory

class ResponsavelFragment : Fragment() {

    private var _binding: FragmentResponsavelBinding? = null
    private val binding get() = _binding!!

    private val responsavelViewModel: ResponsavelViewModel by viewModels {
        ResponsavelViewModelFactory((activity?.application as TransporteApplication).responsavelRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResponsavelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ResponsavelListAdapter()
        binding.recyclerViewResponsaveis.adapter = adapter
        binding.recyclerViewResponsaveis.layoutManager = LinearLayoutManager(context)

        responsavelViewModel.allResponsaveis.asLiveData().observe(viewLifecycleOwner) {
            responsaveis ->
                responsaveis?.let { adapter.submitList(it) }
        }

        binding.buttonSalvarResponsavel.setOnClickListener {
            val nome = binding.editTextNomeResponsavel.text.toString()
            val cpf = binding.editTextCpfResponsavel.text.toString()
            val telefone = binding.editTextTelefoneResponsavel.text.toString()
            val email = binding.editTextEmailResponsavel.text.toString()
            val endereco = binding.editTextEnderecoResponsavel.text.toString()

            if (nome.isNotBlank() && cpf.isNotBlank()) {
                val responsavel = Responsavel(
                    nome = nome, 
                    cpf = cpf, 
                    telefone = telefone, 
                    email = email, 
                    endereco = endereco
                )
                responsavelViewModel.insert(responsavel)

                // Limpar campos após inserção
                binding.editTextNomeResponsavel.text.clear()
                binding.editTextCpfResponsavel.text.clear()
                binding.editTextTelefoneResponsavel.text.clear()
                binding.editTextEmailResponsavel.text.clear()
                binding.editTextEnderecoResponsavel.text.clear()
            } else {
                Toast.makeText(context, "Nome e CPF são obrigatórios", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.fragmentos

import android.app.AlertDialog
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

        val adapter = EscolaListAdapter(
            onDeleteClicked = { escola -> showDeleteConfirmationDialog(escola) },
            onEditClicked = { escola -> escolaViewModel.onEscolaEditClicked(escola) }
        )

        binding.recyclerViewEscolas.adapter = adapter
        binding.recyclerViewEscolas.layoutManager = LinearLayoutManager(context)

        escolaViewModel.allEscolas.asLiveData().observe(viewLifecycleOwner) {
            escolas -> escolas?.let { adapter.submitList(it) }
        }

        escolaViewModel.escolaEmEdicao.observe(viewLifecycleOwner) { escola ->
            if (escola != null) {
                binding.editTextNomeEscola.setText(escola.nome)
                binding.editTextEnderecoEscola.setText(escola.endereco)
                binding.editTextTelefoneEscola.setText(escola.telefone)
                binding.buttonSalvarEscola.text = "Atualizar"
            } else {
                binding.editTextNomeEscola.text.clear()
                binding.editTextEnderecoEscola.text.clear()
                binding.editTextTelefoneEscola.text.clear()
                binding.buttonSalvarEscola.text = "Salvar"
            }
        }

        binding.buttonSalvarEscola.setOnClickListener {
            val nome = binding.editTextNomeEscola.text.toString()
            val endereco = binding.editTextEnderecoEscola.text.toString()
            val telefone = binding.editTextTelefoneEscola.text.toString()

            if (nome.isNotBlank()) {
                val escolaEmEdicao = escolaViewModel.escolaEmEdicao.value
                if (escolaEmEdicao != null) {
                    val escolaAtualizada = escolaEmEdicao.copy(nome = nome, endereco = endereco, telefone = telefone)
                    escolaViewModel.update(escolaAtualizada)
                } else {
                    val novaEscola = Escola(nome = nome, endereco = endereco, telefone = telefone)
                    escolaViewModel.insert(novaEscola)
                }
                escolaViewModel.onEditConcluido()
            } else {
                Toast.makeText(context, "Nome da escola é obrigatório", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showDeleteConfirmationDialog(escola: Escola) {
        AlertDialog.Builder(requireContext())
            .setTitle("Deletar Escola")
            .setMessage("Tem certeza que deseja deletar ${escola.nome}?")
            .setPositiveButton("Sim") { _, _ ->
                escolaViewModel.delete(escola)
            }
            .setNegativeButton("Não", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        escolaViewModel.onEditConcluido()
    }
}
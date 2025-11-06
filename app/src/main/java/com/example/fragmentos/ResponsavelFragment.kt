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

        val adapter = ResponsavelListAdapter(
            onDeleteClicked = { responsavel -> showDeleteConfirmationDialog(responsavel) },
            onEditClicked = { responsavel -> responsavelViewModel.onResponsavelEditClicked(responsavel) }
        )

        binding.recyclerViewResponsaveis.adapter = adapter
        binding.recyclerViewResponsaveis.layoutManager = LinearLayoutManager(context)

        responsavelViewModel.allResponsaveis.asLiveData().observe(viewLifecycleOwner) {
            responsaveis -> responsaveis?.let { adapter.submitList(it) }
        }

        responsavelViewModel.responsavelEmEdicao.observe(viewLifecycleOwner) { responsavel ->
            if (responsavel != null) {
                binding.editTextNomeResponsavel.setText(responsavel.nome)
                binding.editTextCpfResponsavel.setText(responsavel.cpf)
                binding.editTextTelefoneResponsavel.setText(responsavel.telefone)
                binding.editTextEmailResponsavel.setText(responsavel.email)
                binding.editTextEnderecoResponsavel.setText(responsavel.endereco)
                binding.buttonSalvarResponsavel.text = "Atualizar"
            } else {
                binding.editTextNomeResponsavel.text.clear()
                binding.editTextCpfResponsavel.text.clear()
                binding.editTextTelefoneResponsavel.text.clear()
                binding.editTextEmailResponsavel.text.clear()
                binding.editTextEnderecoResponsavel.text.clear()
                binding.buttonSalvarResponsavel.text = "Salvar"
            }
        }

        binding.buttonSalvarResponsavel.setOnClickListener {
            val nome = binding.editTextNomeResponsavel.text.toString()
            val cpf = binding.editTextCpfResponsavel.text.toString()
            val telefone = binding.editTextTelefoneResponsavel.text.toString()
            val email = binding.editTextEmailResponsavel.text.toString()
            val endereco = binding.editTextEnderecoResponsavel.text.toString()

            if (nome.isNotBlank() && cpf.isNotBlank()) {
                val responsavelEmEdicao = responsavelViewModel.responsavelEmEdicao.value
                if (responsavelEmEdicao != null) {
                    val responsavelAtualizado = responsavelEmEdicao.copy(
                        nome = nome, 
                        cpf = cpf, 
                        telefone = telefone, 
                        email = email, 
                        endereco = endereco
                    )
                    responsavelViewModel.update(responsavelAtualizado)
                } else {
                    val novoResponsavel = Responsavel(
                        nome = nome, 
                        cpf = cpf, 
                        telefone = telefone, 
                        email = email, 
                        endereco = endereco
                    )
                    responsavelViewModel.insert(novoResponsavel)
                }
                responsavelViewModel.onEditConcluido()
            } else {
                Toast.makeText(context, "Nome e CPF são obrigatórios", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showDeleteConfirmationDialog(responsavel: Responsavel) {
        AlertDialog.Builder(requireContext())
            .setTitle("Deletar Responsável")
            .setMessage("Tem certeza que deseja deletar ${responsavel.nome}?")
            .setPositiveButton("Sim") { _, _ ->
                responsavelViewModel.delete(responsavel)
            }
            .setNegativeButton("Não", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        responsavelViewModel.onEditConcluido()
    }
}
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
import com.example.fragmentos.databinding.FragmentAlunoBinding
import com.example.fragmentos.db.entity.Aluno
import com.example.fragmentos.ui.aluno.AlunoListAdapter
import com.example.fragmentos.ui.aluno.AlunoViewModel
import com.example.fragmentos.ui.aluno.AlunoViewModelFactory

class AlunoFragment : Fragment() {

    private var _binding: FragmentAlunoBinding? = null
    private val binding get() = _binding!!

    private val alunoViewModel: AlunoViewModel by viewModels {
        AlunoViewModelFactory((activity?.application as TransporteApplication).alunoRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlunoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = AlunoListAdapter(
            onDeleteClicked = { aluno -> showDeleteConfirmationDialog(aluno) },
            onEditClicked = { aluno -> alunoViewModel.onAlunoEditClicked(aluno) }
        )

        binding.recyclerViewAlunos.adapter = adapter
        binding.recyclerViewAlunos.layoutManager = LinearLayoutManager(context)

        alunoViewModel.allAlunos.asLiveData().observe(viewLifecycleOwner) {
            alunos -> alunos?.let { adapter.submitList(it) }
        }

        alunoViewModel.alunoEmEdicao.observe(viewLifecycleOwner) { aluno ->
            if (aluno != null) {
                binding.editTextNomeAluno.setText(aluno.nome)
                binding.editTextDataNascimento.setText(aluno.dataNascimento)
                binding.editTextNomeResponsavel.setText(aluno.nomeResponsavel)
                binding.editTextTelefoneResponsavel.setText(aluno.telefoneResponsavel)
                binding.editTextEndereco.setText(aluno.endereco)
                binding.buttonSalvarAluno.text = "Atualizar"
            } else {
                binding.editTextNomeAluno.text.clear()
                binding.editTextDataNascimento.text.clear()
                binding.editTextNomeResponsavel.text.clear()
                binding.editTextTelefoneResponsavel.text.clear()
                binding.editTextEndereco.text.clear()
                binding.buttonSalvarAluno.text = "Salvar"
            }
        }

        binding.buttonSalvarAluno.setOnClickListener {
            val nome = binding.editTextNomeAluno.text.toString()
            val dataNascimento = binding.editTextDataNascimento.text.toString()
            val nomeResponsavel = binding.editTextNomeResponsavel.text.toString()
            val telefoneResponsavel = binding.editTextTelefoneResponsavel.text.toString()
            val endereco = binding.editTextEndereco.text.toString()

            if (nome.isNotBlank() && nomeResponsavel.isNotBlank()) {
                val alunoEmEdicao = alunoViewModel.alunoEmEdicao.value
                if (alunoEmEdicao != null) {
                    val alunoAtualizado = alunoEmEdicao.copy(
                        nome = nome, 
                        dataNascimento = dataNascimento, 
                        nomeResponsavel = nomeResponsavel, 
                        telefoneResponsavel = telefoneResponsavel, 
                        endereco = endereco
                    )
                    alunoViewModel.update(alunoAtualizado)
                } else {
                    val novoAluno = Aluno(
                        nome = nome, 
                        dataNascimento = dataNascimento, 
                        nomeResponsavel = nomeResponsavel, 
                        telefoneResponsavel = telefoneResponsavel, 
                        endereco = endereco
                    )
                    alunoViewModel.insert(novoAluno)
                }
                alunoViewModel.onEditConcluido() // Limpa o formulário e reseta o botão
            } else {
                Toast.makeText(context, "Nome e nome do responsável são obrigatórios", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showDeleteConfirmationDialog(aluno: Aluno) {
        AlertDialog.Builder(requireContext())
            .setTitle("Deletar Aluno")
            .setMessage("Tem certeza que deseja deletar ${aluno.nome}?")
            .setPositiveButton("Sim") { _, _ ->
                alunoViewModel.delete(aluno)
            }
            .setNegativeButton("Não", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        alunoViewModel.onEditConcluido() // Garante que o modo de edição seja limpo ao sair da tela
    }
}
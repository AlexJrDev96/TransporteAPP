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

        val adapter = AlunoListAdapter()
        binding.recyclerViewAlunos.adapter = adapter
        binding.recyclerViewAlunos.layoutManager = LinearLayoutManager(context)

        alunoViewModel.allAlunos.asLiveData().observe(viewLifecycleOwner) {
            alunos ->
                alunos?.let { adapter.submitList(it) }
        }

        binding.buttonSalvarAluno.setOnClickListener {
            val nome = binding.editTextNomeAluno.text.toString()
            val dataNascimento = binding.editTextDataNascimento.text.toString()
            val nomeResponsavel = binding.editTextNomeResponsavel.text.toString()
            val telefoneResponsavel = binding.editTextTelefoneResponsavel.text.toString()
            val endereco = binding.editTextEndereco.text.toString()

            if (nome.isNotBlank() && nomeResponsavel.isNotBlank()) {
                val aluno = Aluno(
                    nome = nome, 
                    dataNascimento = dataNascimento, 
                    nomeResponsavel = nomeResponsavel, 
                    telefoneResponsavel = telefoneResponsavel, 
                    endereco = endereco
                )
                alunoViewModel.insert(aluno)

                // Limpar campos após inserção
                binding.editTextNomeAluno.text.clear()
                binding.editTextDataNascimento.text.clear()
                binding.editTextNomeResponsavel.text.clear()
                binding.editTextTelefoneResponsavel.text.clear()
                binding.editTextEndereco.text.clear()
            } else {
                Toast.makeText(context, "Nome e nome do responsável são obrigatórios", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.fragmentos

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fragmentos.databinding.FragmentAlunoBinding
import com.example.fragmentos.db.entity.Aluno
import com.example.fragmentos.db.entity.Turma
import com.example.fragmentos.ui.aluno.AlunoListAdapter
import com.example.fragmentos.ui.aluno.AlunoViewModel
import com.example.fragmentos.ui.aluno.AlunoViewModelFactory

class AlunoFragment : Fragment() {

    private var _binding: FragmentAlunoBinding? = null
    private val binding get() = _binding!!

    private val alunoViewModel: AlunoViewModel by viewModels {
        val application = requireActivity().application as TransporteApplication
        AlunoViewModelFactory(application.alunoRepository, application.turmaRepository)
    }

    private lateinit var turmas: List<Turma>
    private var selectedTurmaId: Int? = null

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

        setupObservers(adapter)
        setupUI()
    }

    private fun setupObservers(adapter: AlunoListAdapter) {
        // Observa a lista de turmas para preencher o Spinner
        alunoViewModel.allTurmas.asLiveData().observe(viewLifecycleOwner) { turmasList ->
            if (turmasList == null) return@observe
            turmas = turmasList
            val nomesTurmas = turmasList.map { it.nome }
            val turmaAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, nomesTurmas)
            turmaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerTurma.adapter = turmaAdapter

            // Se estiver em modo de edição, tenta pré-selecionar a turma correta agora que a lista está pronta
            alunoViewModel.alunoEmEdicao.value?.let { aluno ->
                val turmaPosition = turmas.indexOfFirst { it.id == aluno.turmaId }
                if (turmaPosition >= 0) {
                    binding.spinnerTurma.setSelection(turmaPosition)
                }
            }
        }

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

                // Garante que o spinner seja atualizado se a lista de turmas já estiver carregada
                if (::turmas.isInitialized) {
                    val turmaPosition = turmas.indexOfFirst { it.id == aluno.turmaId }
                    if (turmaPosition >= 0) {
                        binding.spinnerTurma.setSelection(turmaPosition)
                    }
                }
            } else {
                binding.editTextNomeAluno.text.clear()
                binding.editTextDataNascimento.text.clear()
                binding.editTextNomeResponsavel.text.clear()
                binding.editTextTelefoneResponsavel.text.clear()
                binding.editTextEndereco.text.clear()
                if (::turmas.isInitialized && turmas.isNotEmpty()) binding.spinnerTurma.setSelection(0)
                binding.buttonSalvarAluno.text = "Salvar"
            }
        }
    }

    private fun setupUI() {
        binding.spinnerTurma.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (::turmas.isInitialized && turmas.isNotEmpty()) {
                    selectedTurmaId = turmas[position].id
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedTurmaId = null
            }
        }

        binding.buttonSalvarAluno.setOnClickListener {
            val nome = binding.editTextNomeAluno.text.toString()
            val dataNascimento = binding.editTextDataNascimento.text.toString()
            val nomeResponsavel = binding.editTextNomeResponsavel.text.toString()
            val telefoneResponsavel = binding.editTextTelefoneResponsavel.text.toString()
            val endereco = binding.editTextEndereco.text.toString()

            if (nome.isBlank() || selectedTurmaId == null) {
                Toast.makeText(context, "Nome e Turma são obrigatórios", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val alunoEmEdicao = alunoViewModel.alunoEmEdicao.value
            val newOrUpdatedAluno = Aluno(
                id = alunoEmEdicao?.id ?: 0,
                nome = nome,
                dataNascimento = dataNascimento,
                nomeResponsavel = nomeResponsavel,
                telefoneResponsavel = telefoneResponsavel,
                endereco = endereco,
                turmaId = selectedTurmaId!!
            )

            if (alunoEmEdicao != null) {
                alunoViewModel.update(newOrUpdatedAluno)
            } else {
                alunoViewModel.insert(newOrUpdatedAluno)
            }
            alunoViewModel.onEditConcluido()
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
        alunoViewModel.onEditConcluido()
    }
}
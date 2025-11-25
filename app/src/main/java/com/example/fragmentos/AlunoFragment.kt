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
import com.example.fragmentos.api.Endereco
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
        setupListeners()
    }

    private fun setupObservers(adapter: AlunoListAdapter) {
        alunoViewModel.allTurmas.asLiveData().observe(viewLifecycleOwner) { turmasList ->
            if (turmasList == null) return@observe
            turmas = turmasList
            val nomesTurmas = turmasList.map { it.nome }
            val turmaAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, nomesTurmas)
            turmaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerTurma.adapter = turmaAdapter

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
                binding.editTextCepAluno.setText(aluno.cep)
                binding.editTextLogradouroAluno.setText(aluno.logradouro)
                binding.editTextBairroAluno.setText(aluno.bairro)
                binding.editTextNumeroAluno.setText(aluno.numero)
                binding.buttonSalvarAluno.text = "Atualizar"

                if (::turmas.isInitialized) {
                    val turmaPosition = turmas.indexOfFirst { it.id == aluno.turmaId }
                    if (turmaPosition >= 0) {
                        binding.spinnerTurma.setSelection(turmaPosition)
                    }
                }
            } else {
                clearForm()
            }
        }

        alunoViewModel.enderecoEncontrado.observe(viewLifecycleOwner) { endereco ->
            if (endereco != null) {
                binding.editTextLogradouroAluno.setText(endereco.logradouro)
                binding.editTextBairroAluno.setText(endereco.bairro)
            } else {
                Toast.makeText(context, "CEP não encontrado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupListeners() {
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

        binding.buttonBuscarCepAluno.setOnClickListener {
            val cep = binding.editTextCepAluno.text.toString()
            if (cep.length == 8) {
                alunoViewModel.buscaEnderecoPorCep(cep)
            } else {
                Toast.makeText(context, "Digite um CEP válido com 8 dígitos", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonSalvarAluno.setOnClickListener {
            saveAluno()
        }
    }

    private fun saveAluno() {
        val nome = binding.editTextNomeAluno.text.toString()
        val dataNascimento = binding.editTextDataNascimento.text.toString()
        val nomeResponsavel = binding.editTextNomeResponsavel.text.toString()
        val telefoneResponsavel = binding.editTextTelefoneResponsavel.text.toString()
        val cep = binding.editTextCepAluno.text.toString()
        val logradouro = binding.editTextLogradouroAluno.text.toString()
        val bairro = binding.editTextBairroAluno.text.toString()
        val numero = binding.editTextNumeroAluno.text.toString()

        if (nome.isBlank() || selectedTurmaId == null) {
            Toast.makeText(context, "Nome e Turma são obrigatórios", Toast.LENGTH_LONG).show()
            return
        }

        val alunoEmEdicao = alunoViewModel.alunoEmEdicao.value
        val newOrUpdatedAluno = Aluno(
            id = alunoEmEdicao?.id ?: 0,
            nome = nome,
            dataNascimento = dataNascimento,
            nomeResponsavel = nomeResponsavel,
            telefoneResponsavel = telefoneResponsavel,
            cep = cep,
            logradouro = logradouro,
            bairro = bairro,
            numero = numero,
            turmaId = selectedTurmaId!!
        )

        if (alunoEmEdicao != null) {
            alunoViewModel.update(newOrUpdatedAluno)
        } else {
            alunoViewModel.insert(newOrUpdatedAluno)
        }
        alunoViewModel.onEditConcluido()
    }

    private fun clearForm() {
        binding.editTextNomeAluno.text.clear()
        binding.editTextDataNascimento.text.clear()
        binding.editTextNomeResponsavel.text.clear()
        binding.editTextTelefoneResponsavel.text.clear()
        binding.editTextCepAluno.text.clear()
        binding.editTextLogradouroAluno.text.clear()
        binding.editTextBairroAluno.text.clear()
        binding.editTextNumeroAluno.text.clear()
        if (::turmas.isInitialized && turmas.isNotEmpty()) binding.spinnerTurma.setSelection(0)
        binding.buttonSalvarAluno.text = "Salvar"
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

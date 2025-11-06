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
import com.example.fragmentos.databinding.FragmentTurmaBinding
import com.example.fragmentos.db.entity.Escola
import com.example.fragmentos.db.entity.Tripulante
import com.example.fragmentos.db.entity.Turma
import com.example.fragmentos.ui.turma.TurmaListAdapter
import com.example.fragmentos.ui.turma.TurmaViewModel
import com.example.fragmentos.ui.turma.TurmaViewModelFactory

class TurmaFragment : Fragment() {

    private var _binding: FragmentTurmaBinding? = null
    private val binding get() = _binding!!

    private val turmaViewModel: TurmaViewModel by viewModels {
        val application = requireActivity().application as TransporteApplication
        TurmaViewModelFactory(application.turmaRepository, application.escolaRepository, application.tripulanteRepository)
    }

    private lateinit var escolas: List<Escola>
    private lateinit var tripulantes: List<Tripulante>
    private var selectedEscolaId: Int? = null
    private var selectedTripulanteId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTurmaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TurmaListAdapter(
            onDeleteClicked = { turma -> showDeleteConfirmationDialog(turma) },
            onEditClicked = { turma -> turmaViewModel.onTurmaEditClicked(turma) }
        )

        binding.recyclerViewTurmas.adapter = adapter
        binding.recyclerViewTurmas.layoutManager = LinearLayoutManager(context)

        setupObservers(adapter)
        setupUI()
    }

    private fun setupObservers(adapter: TurmaListAdapter) {
        turmaViewModel.allTurmas.asLiveData().observe(viewLifecycleOwner) {
            turmas -> turmas?.let { adapter.submitList(it) }
        }

        turmaViewModel.allEscolas.asLiveData().observe(viewLifecycleOwner) { escolasList ->
            escolas = escolasList
            val nomesEscolas = escolasList.map { it.nome }
            val escolaAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, nomesEscolas)
            escolaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerEscola.adapter = escolaAdapter
        }

        turmaViewModel.allTripulantes.asLiveData().observe(viewLifecycleOwner) { tripulantesList ->
            tripulantes = tripulantesList
            val nomesTripulantes = tripulantesList.map { it.nome }
            val tripulanteAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, nomesTripulantes)
            tripulanteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerTripulante.adapter = tripulanteAdapter
        }

        turmaViewModel.turmaEmEdicao.observe(viewLifecycleOwner) { turma ->
            if (turma != null) {
                binding.editTextNomeTurma.setText(turma.nome)
                val escolaPosition = escolas.indexOfFirst { it.id == turma.escolaId }
                if (escolaPosition >= 0) {
                    binding.spinnerEscola.setSelection(escolaPosition)
                }
                val tripulantePosition = tripulantes.indexOfFirst { it.id == turma.tripulanteId }
                if (tripulantePosition >= 0) {
                    binding.spinnerTripulante.setSelection(tripulantePosition)
                }
                binding.buttonSalvarTurma.text = "Atualizar"
            } else {
                binding.editTextNomeTurma.text.clear()
                if (::escolas.isInitialized && escolas.isNotEmpty()) binding.spinnerEscola.setSelection(0)
                if (::tripulantes.isInitialized && tripulantes.isNotEmpty()) binding.spinnerTripulante.setSelection(0)
                binding.buttonSalvarTurma.text = "Salvar"
            }
        }
    }

    private fun setupUI() {
        binding.spinnerEscola.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (::escolas.isInitialized && escolas.isNotEmpty()) {
                    selectedEscolaId = escolas[position].id
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedEscolaId = null
            }
        }

        binding.spinnerTripulante.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (::tripulantes.isInitialized && tripulantes.isNotEmpty()) {
                    selectedTripulanteId = tripulantes[position].id
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedTripulanteId = null
            }
        }

        binding.buttonSalvarTurma.setOnClickListener {
            val nome = binding.editTextNomeTurma.text.toString()

            if (nome.isBlank() || selectedEscolaId == null || selectedTripulanteId == null) {
                Toast.makeText(context, "Todos os campos são obrigatórios", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val turmaEmEdicao = turmaViewModel.turmaEmEdicao.value
            if (turmaEmEdicao != null) {
                val turmaAtualizada = turmaEmEdicao.copy(nome = nome, escolaId = selectedEscolaId!!, tripulanteId = selectedTripulanteId!!)
                turmaViewModel.update(turmaAtualizada)
            } else {
                val novaTurma = Turma(nome = nome, escolaId = selectedEscolaId!!, tripulanteId = selectedTripulanteId!!)
                turmaViewModel.insert(novaTurma)
            }
            turmaViewModel.onEditConcluido()
        }
    }

    private fun showDeleteConfirmationDialog(turma: Turma) {
        AlertDialog.Builder(requireContext())
            .setTitle("Deletar Turma")
            .setMessage("Tem certeza que deseja deletar ${turma.nome}?")
            .setPositiveButton("Sim") { _, _ ->
                turmaViewModel.delete(turma)
            }
            .setNegativeButton("Não", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        turmaViewModel.onEditConcluido()
    }
}
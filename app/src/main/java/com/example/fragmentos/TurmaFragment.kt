package com.example.fragmentos

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

    private var escolas: List<Escola> = emptyList()
    private var tripulantes: List<Tripulante> = emptyList()
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

        val adapter = TurmaListAdapter()
        binding.recyclerViewTurmas.adapter = adapter
        binding.recyclerViewTurmas.layoutManager = LinearLayoutManager(context)

        turmaViewModel.allTurmas.asLiveData().observe(viewLifecycleOwner) { turmas ->
            turmas?.let { adapter.submitList(it) }
        }

        setupEscolaSpinner()
        setupTripulanteSpinner()

        binding.buttonSalvarTurma.setOnClickListener {
            val nome = binding.editTextNomeTurma.text.toString()

            if (escolas.isEmpty()) {
                Toast.makeText(context, "Cadastre uma escola primeiro!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (tripulantes.isEmpty()) {
                Toast.makeText(context, "Cadastre um tripulante primeiro!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (nome.isNotBlank() && selectedEscolaId != null && selectedTripulanteId != null) {
                val turma = Turma(
                    nome = nome,
                    escolaId = selectedEscolaId!!,
                    tripulanteId = selectedTripulanteId!!
                )
                turmaViewModel.insert(turma)

                binding.editTextNomeTurma.text.clear()
                binding.spinnerEscola.setSelection(0)
                binding.spinnerTripulante.setSelection(0)
            } else {
                Toast.makeText(context, "O nome da turma é obrigatório", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupEscolaSpinner() {
        val escolaAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item)
        escolaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerEscola.adapter = escolaAdapter

        turmaViewModel.allEscolas.asLiveData().observe(viewLifecycleOwner) { escolasList ->
            escolas = escolasList
            val nomesEscolas = escolasList.map { it.nome }
            escolaAdapter.clear()
            escolaAdapter.addAll(nomesEscolas)
            escolaAdapter.notifyDataSetChanged()
        }

        binding.spinnerEscola.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (escolas.isNotEmpty()) {
                    selectedEscolaId = escolas[position].id
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedEscolaId = null
            }
        }
    }

    private fun setupTripulanteSpinner() {
        val tripulanteAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item)
        tripulanteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerTripulante.adapter = tripulanteAdapter

        turmaViewModel.allTripulantes.asLiveData().observe(viewLifecycleOwner) { tripulantesList ->
            tripulantes = tripulantesList
            val nomesTripulantes = tripulantesList.map { it.nome }
            tripulanteAdapter.clear()
            tripulanteAdapter.addAll(nomesTripulantes)
            tripulanteAdapter.notifyDataSetChanged()
        }

        binding.spinnerTripulante.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (tripulantes.isNotEmpty()) {
                    selectedTripulanteId = tripulantes[position].id
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedTripulanteId = null
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
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
import com.example.fragmentos.databinding.FragmentTripulanteBinding
import com.example.fragmentos.db.entity.Tripulante
import com.example.fragmentos.ui.tripulante.TripulanteListAdapter
import com.example.fragmentos.ui.tripulante.TripulanteViewModel
import com.example.fragmentos.ui.tripulante.TripulanteViewModelFactory

class TripulanteFragment : Fragment() {

    private var _binding: FragmentTripulanteBinding? = null
    private val binding get() = _binding!!

    private val tripulanteViewModel: TripulanteViewModel by viewModels {
        TripulanteViewModelFactory((activity?.application as TransporteApplication).tripulanteRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTripulanteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TripulanteListAdapter()
        binding.recyclerViewTripulantes.adapter = adapter
        binding.recyclerViewTripulantes.layoutManager = LinearLayoutManager(context)

        tripulanteViewModel.allTripulantes.asLiveData().observe(viewLifecycleOwner) {
            tripulantes ->
                tripulantes?.let { adapter.submitList(it) }
        }

        binding.buttonSalvar.setOnClickListener {
            val nome = binding.editTextNome.text.toString()
            val funcao = binding.editTextFuncao.text.toString()
            val telefone = binding.editTextTelefone.text.toString()
            val cpf = binding.editTextCpf.text.toString()

            if (nome.isNotBlank() && funcao.isNotBlank()) {
                val tripulante = Tripulante(nome = nome, funcao = funcao, telefone = telefone, cpf = cpf)
                tripulanteViewModel.insert(tripulante)

                // Limpar campos após inserção
                binding.editTextNome.text.clear()
                binding.editTextFuncao.text.clear()
                binding.editTextTelefone.text.clear()
                binding.editTextCpf.text.clear()
            } else {
                Toast.makeText(context, "Nome e função são obrigatórios", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
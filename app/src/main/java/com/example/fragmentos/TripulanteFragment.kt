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

        val adapter = TripulanteListAdapter(
            onDeleteClicked = { tripulante -> showDeleteConfirmationDialog(tripulante) },
            onEditClicked = { tripulante -> tripulanteViewModel.onTripulanteEditClicked(tripulante) }
        )

        binding.recyclerViewTripulantes.adapter = adapter
        binding.recyclerViewTripulantes.layoutManager = LinearLayoutManager(context)

        tripulanteViewModel.allTripulantes.asLiveData().observe(viewLifecycleOwner) {
            tripulantes ->
                tripulantes?.let { adapter.submitList(it) }
        }

        tripulanteViewModel.tripulanteEmEdicao.observe(viewLifecycleOwner) { tripulante ->
            if (tripulante != null) {
                binding.editTextNome.setText(tripulante.nome)
                binding.editTextFuncao.setText(tripulante.funcao)
                binding.editTextTelefone.setText(tripulante.telefone)
                binding.editTextCpf.setText(tripulante.cpf)
                binding.buttonSalvar.text = "Atualizar"
            } else {
                binding.editTextNome.text.clear()
                binding.editTextFuncao.text.clear()
                binding.editTextTelefone.text.clear()
                binding.editTextCpf.text.clear()
                binding.buttonSalvar.text = "Salvar"
            }
        }

        binding.buttonSalvar.setOnClickListener {
            val nome = binding.editTextNome.text.toString()
            val funcao = binding.editTextFuncao.text.toString()
            val telefone = binding.editTextTelefone.text.toString()
            val cpf = binding.editTextCpf.text.toString()

            if (nome.isNotBlank() && funcao.isNotBlank()) {
                val tripulanteEmEdicao = tripulanteViewModel.tripulanteEmEdicao.value
                if (tripulanteEmEdicao != null) {
                    val tripulanteAtualizado = tripulanteEmEdicao.copy(nome = nome, funcao = funcao, telefone = telefone, cpf = cpf)
                    tripulanteViewModel.update(tripulanteAtualizado)
                } else {
                    val novoTripulante = Tripulante(nome = nome, funcao = funcao, telefone = telefone, cpf = cpf)
                    tripulanteViewModel.insert(novoTripulante)
                }
                tripulanteViewModel.onEditConcluido() // Limpa o formulário e reseta o botão
            } else {
                Toast.makeText(context, "Nome e função são obrigatórios", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showDeleteConfirmationDialog(tripulante: Tripulante) {
        AlertDialog.Builder(requireContext())
            .setTitle("Deletar Tripulante")
            .setMessage("Tem certeza que deseja deletar ${tripulante.nome}?")
            .setPositiveButton("Sim") { _, _ ->
                tripulanteViewModel.delete(tripulante)
            }
            .setNegativeButton("Não", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        tripulanteViewModel.onEditConcluido() // Garante que o modo de edição seja limpo ao sair da tela
    }
}
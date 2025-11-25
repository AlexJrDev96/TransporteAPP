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

        setupObservers(adapter)
        setupListeners()
    }

    private fun setupObservers(adapter: ResponsavelListAdapter) {
        responsavelViewModel.allResponsaveis.asLiveData().observe(viewLifecycleOwner) {
            responsaveis -> responsaveis?.let { adapter.submitList(it) }
        }

        responsavelViewModel.responsavelEmEdicao.observe(viewLifecycleOwner) { responsavel ->
            if (responsavel != null) {
                binding.editTextNomeResponsavel.setText(responsavel.nome)
                binding.editTextCpfResponsavel.setText(responsavel.cpf)
                binding.editTextCepResponsavel.setText(responsavel.cep)
                binding.editTextLogradouroResponsavel.setText(responsavel.logradouro)
                binding.editTextBairroResponsavel.setText(responsavel.bairro)
                binding.editTextNumeroResponsavel.setText(responsavel.numero)
                binding.editTextTelefoneResponsavel.setText(responsavel.telefone)
                binding.editTextEmailResponsavel.setText(responsavel.email)
                binding.buttonSalvarResponsavel.text = "Atualizar"
            } else {
                clearForm()
            }
        }

        // Observador para a resposta da API
        responsavelViewModel.enderecoEncontrado.observe(viewLifecycleOwner) { endereco ->
            if (endereco != null) {
                binding.editTextLogradouroResponsavel.setText(endereco.logradouro)
                binding.editTextBairroResponsavel.setText(endereco.bairro)
            } else {
                Toast.makeText(context, "CEP não encontrado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupListeners() {
        // Conecta a busca da API ao clique do botão
        binding.buttonBuscarCep.setOnClickListener {
            val cep = binding.editTextCepResponsavel.text.toString()
            if (cep.length == 8) {
                responsavelViewModel.buscaEnderecoPorCep(cep)
            } else {
                Toast.makeText(context, "Digite um CEP válido com 8 dígitos", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonSalvarResponsavel.setOnClickListener {
            saveResponsavel()
        }
    }

    private fun saveResponsavel() {
        val nome = binding.editTextNomeResponsavel.text.toString()
        val cpf = binding.editTextCpfResponsavel.text.toString()
        val cep = binding.editTextCepResponsavel.text.toString()
        val logradouro = binding.editTextLogradouroResponsavel.text.toString()
        val bairro = binding.editTextBairroResponsavel.text.toString()
        val numero = binding.editTextNumeroResponsavel.text.toString()
        val telefone = binding.editTextTelefoneResponsavel.text.toString()
        val email = binding.editTextEmailResponsavel.text.toString()

        if (nome.isNotBlank() && cpf.isNotBlank()) {
            val responsavelEmEdicao = responsavelViewModel.responsavelEmEdicao.value
            val newOrUpdatedResponsavel = Responsavel(
                id = responsavelEmEdicao?.id ?: 0,
                nome = nome, 
                cpf = cpf, 
                cep = cep,
                logradouro = logradouro,
                bairro = bairro,
                numero = numero,
                telefone = telefone, 
                email = email
            )
            if (responsavelEmEdicao != null) {
                responsavelViewModel.update(newOrUpdatedResponsavel)
            } else {
                responsavelViewModel.insert(newOrUpdatedResponsavel)
            }
            responsavelViewModel.onEditConcluido()
        } else {
            Toast.makeText(context, "Nome e CPF são obrigatórios", Toast.LENGTH_LONG).show()
        }
    }

    private fun clearForm() {
        binding.editTextNomeResponsavel.text.clear()
        binding.editTextCpfResponsavel.text.clear()
        binding.editTextCepResponsavel.text.clear()
        binding.editTextLogradouroResponsavel.text.clear()
        binding.editTextBairroResponsavel.text.clear()
        binding.editTextNumeroResponsavel.text.clear()
        binding.editTextTelefoneResponsavel.text.clear()
        binding.editTextEmailResponsavel.text.clear()
        binding.buttonSalvarResponsavel.text = "Salvar"
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
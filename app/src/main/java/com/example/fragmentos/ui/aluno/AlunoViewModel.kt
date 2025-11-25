package com.example.fragmentos.ui.aluno

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fragmentos.api.ApiClient
import com.example.fragmentos.api.Endereco
import com.example.fragmentos.db.entity.Aluno
import com.example.fragmentos.db.entity.Turma
import com.example.fragmentos.db.repository.AlunoRepository
import com.example.fragmentos.db.repository.TurmaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AlunoViewModel(
    private val alunoRepository: AlunoRepository,
    private val turmaRepository: TurmaRepository
) : ViewModel() {

    val allAlunos: Flow<List<Aluno>> = alunoRepository.allAlunos
    val allTurmas: Flow<List<Turma>> = turmaRepository.allTurmas

    private val _alunoEmEdicao = MutableLiveData<Aluno?>()
    val alunoEmEdicao: LiveData<Aluno?> = _alunoEmEdicao

    // LiveData para expor o endereço encontrado para o Fragment
    private val _enderecoEncontrado = MutableLiveData<Endereco?>()
    val enderecoEncontrado: LiveData<Endereco?> = _enderecoEncontrado

    /**
     * Busca o endereço usando o ApiClient centralizado.
     */
    fun buscaEnderecoPorCep(cep: String) {
        viewModelScope.launch {
            try {
                // Chama a API através do ApiClient singleton
                val endereco = ApiClient.viaCepService.getEndereco(cep)
                _enderecoEncontrado.postValue(endereco)
            } catch (e: Exception) {
                _enderecoEncontrado.postValue(null)
                Log.e("AlunoViewModel", "Erro ao buscar CEP: ${e.message}")
            }
        }
    }

    fun insert(aluno: Aluno) = viewModelScope.launch {
        alunoRepository.insert(aluno)
    }

    fun update(aluno: Aluno) = viewModelScope.launch {
        alunoRepository.update(aluno)
    }

    fun delete(aluno: Aluno) = viewModelScope.launch {
        alunoRepository.delete(aluno)
    }

    fun onAlunoEditClicked(aluno: Aluno) {
        _alunoEmEdicao.value = aluno
    }

    fun onEditConcluido() {
        _alunoEmEdicao.value = null
    }
}

class AlunoViewModelFactory(
    private val alunoRepository: AlunoRepository,
    private val turmaRepository: TurmaRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlunoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AlunoViewModel(alunoRepository, turmaRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
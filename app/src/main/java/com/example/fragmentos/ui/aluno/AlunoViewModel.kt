package com.example.fragmentos.ui.aluno

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fragmentos.db.entity.Aluno
import com.example.fragmentos.db.repository.AlunoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AlunoViewModel(private val repository: AlunoRepository) : ViewModel() {

    val allAlunos: Flow<List<Aluno>> = repository.allAlunos

    private val _alunoEmEdicao = MutableLiveData<Aluno?>()
    val alunoEmEdicao: LiveData<Aluno?> = _alunoEmEdicao

    fun insert(aluno: Aluno) = viewModelScope.launch {
        repository.insert(aluno)
    }

    fun update(aluno: Aluno) = viewModelScope.launch {
        repository.update(aluno)
    }

    fun delete(aluno: Aluno) = viewModelScope.launch {
        repository.delete(aluno)
    }

    fun onAlunoEditClicked(aluno: Aluno) {
        _alunoEmEdicao.value = aluno
    }

    fun onEditConcluido() {
        _alunoEmEdicao.value = null
    }
}

class AlunoViewModelFactory(private val repository: AlunoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlunoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AlunoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
package com.example.fragmentos.ui.aluno

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fragmentos.db.entity.Aluno
import com.example.fragmentos.db.repository.AlunoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AlunoViewModel(private val repository: AlunoRepository) : ViewModel() {

    val allAlunos: Flow<List<Aluno>> = repository.allAlunos

    fun insert(aluno: Aluno) = viewModelScope.launch {
        repository.insert(aluno)
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
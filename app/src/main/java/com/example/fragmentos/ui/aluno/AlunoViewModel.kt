package com.example.fragmentos.ui.aluno

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
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
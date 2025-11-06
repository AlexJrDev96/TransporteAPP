package com.example.fragmentos.ui.turma

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fragmentos.db.entity.Escola
import com.example.fragmentos.db.entity.Tripulante
import com.example.fragmentos.db.entity.Turma
import com.example.fragmentos.db.repository.EscolaRepository
import com.example.fragmentos.db.repository.TripulanteRepository
import com.example.fragmentos.db.repository.TurmaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TurmaViewModel(
    private val turmaRepository: TurmaRepository, 
    private val escolaRepository: EscolaRepository, 
    private val tripulanteRepository: TripulanteRepository
) : ViewModel() {

    val allTurmas: Flow<List<Turma>> = turmaRepository.allTurmas
    val allEscolas: Flow<List<Escola>> = escolaRepository.allEscolas
    val allTripulantes: Flow<List<Tripulante>> = tripulanteRepository.allTripulantes

    private val _turmaEmEdicao = MutableLiveData<Turma?>()
    val turmaEmEdicao: LiveData<Turma?> = _turmaEmEdicao

    fun insert(turma: Turma) = viewModelScope.launch {
        turmaRepository.insert(turma)
    }

    fun update(turma: Turma) = viewModelScope.launch {
        turmaRepository.update(turma)
    }

    fun delete(turma: Turma) = viewModelScope.launch {
        turmaRepository.delete(turma)
    }

    fun onTurmaEditClicked(turma: Turma) {
        _turmaEmEdicao.value = turma
    }

    fun onEditConcluido() {
        _turmaEmEdicao.value = null
    }
}

class TurmaViewModelFactory(
    private val turmaRepository: TurmaRepository, 
    private val escolaRepository: EscolaRepository, 
    private val tripulanteRepository: TripulanteRepository
    ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TurmaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TurmaViewModel(turmaRepository, escolaRepository, tripulanteRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
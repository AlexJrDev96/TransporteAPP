package com.example.fragmentos.ui.tripulante

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fragmentos.db.entity.Tripulante
import com.example.fragmentos.db.repository.TripulanteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TripulanteViewModel(private val repository: TripulanteRepository) : ViewModel() {

    val allTripulantes: Flow<List<Tripulante>> = repository.allTripulantes

    private val _tripulanteEmEdicao = MutableLiveData<Tripulante?>()
    val tripulanteEmEdicao: LiveData<Tripulante?> = _tripulanteEmEdicao

    fun insert(tripulante: Tripulante) = viewModelScope.launch {
        repository.insert(tripulante)
    }

    fun update(tripulante: Tripulante) = viewModelScope.launch {
        repository.update(tripulante)
    }

    fun delete(tripulante: Tripulante) = viewModelScope.launch {
        repository.delete(tripulante)
    }

    fun onTripulanteEditClicked(tripulante: Tripulante) {
        _tripulanteEmEdicao.value = tripulante
    }

    fun onEditConcluido() {
        _tripulanteEmEdicao.value = null
    }
}

class TripulanteViewModelFactory(private val repository: TripulanteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TripulanteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TripulanteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
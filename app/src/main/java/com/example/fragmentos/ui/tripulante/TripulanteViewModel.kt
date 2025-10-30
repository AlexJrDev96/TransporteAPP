package com.example.fragmentos.ui.tripulante

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fragmentos.db.entity.Tripulante
import com.example.fragmentos.db.repository.TripulanteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TripulanteViewModel(private val repository: TripulanteRepository) : ViewModel() {

    val allTripulantes: Flow<List<Tripulante>> = repository.allTripulantes

    fun insert(tripulante: Tripulante) = viewModelScope.launch {
        repository.insert(tripulante)
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
package com.example.fragmentos.ui.escola

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fragmentos.db.entity.Escola
import com.example.fragmentos.db.repository.EscolaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class EscolaViewModel(private val repository: EscolaRepository) : ViewModel() {

    val allEscolas: Flow<List<Escola>> = repository.allEscolas

    fun insert(escola: Escola) = viewModelScope.launch {
        repository.insert(escola)
    }
}

class EscolaViewModelFactory(private val repository: EscolaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EscolaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EscolaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
package com.example.fragmentos.ui.escola

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fragmentos.db.entity.Escola
import com.example.fragmentos.db.repository.EscolaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class EscolaViewModel(private val repository: EscolaRepository) : ViewModel() {

    val allEscolas: Flow<List<Escola>> = repository.allEscolas

    private val _escolaEmEdicao = MutableLiveData<Escola?>()
    val escolaEmEdicao: LiveData<Escola?> = _escolaEmEdicao

    fun insert(escola: Escola) = viewModelScope.launch {
        repository.insert(escola)
    }

    fun update(escola: Escola) = viewModelScope.launch {
        repository.update(escola)
    }

    fun delete(escola: Escola) = viewModelScope.launch {
        repository.delete(escola)
    }

    fun onEscolaEditClicked(escola: Escola) {
        _escolaEmEdicao.value = escola
    }

    fun onEditConcluido() {
        _escolaEmEdicao.value = null
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
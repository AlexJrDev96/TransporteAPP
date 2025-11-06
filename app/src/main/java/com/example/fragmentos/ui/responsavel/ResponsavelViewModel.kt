package com.example.fragmentos.ui.responsavel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fragmentos.db.entity.Responsavel
import com.example.fragmentos.db.repository.ResponsavelRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ResponsavelViewModel(private val repository: ResponsavelRepository) : ViewModel() {

    val allResponsaveis: Flow<List<Responsavel>> = repository.allResponsaveis

    private val _responsavelEmEdicao = MutableLiveData<Responsavel?>()
    val responsavelEmEdicao: LiveData<Responsavel?> = _responsavelEmEdicao

    fun insert(responsavel: Responsavel) = viewModelScope.launch {
        repository.insert(responsavel)
    }

    fun update(responsavel: Responsavel) = viewModelScope.launch {
        repository.update(responsavel)
    }

    fun delete(responsavel: Responsavel) = viewModelScope.launch {
        repository.delete(responsavel)
    }

    fun onResponsavelEditClicked(responsavel: Responsavel) {
        _responsavelEmEdicao.value = responsavel
    }

    fun onEditConcluido() {
        _responsavelEmEdicao.value = null
    }
}

class ResponsavelViewModelFactory(private val repository: ResponsavelRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResponsavelViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ResponsavelViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
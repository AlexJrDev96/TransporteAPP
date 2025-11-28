package com.example.fragmentos.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fragmentos.db.entity.User
import com.example.fragmentos.db.repository.UserRepository
import com.example.fragmentos.util.PasswordHasher
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(email: String, password: String) = viewModelScope.launch {
        val user = repository.getUserByEmail(email)

        // Criptografa a senha digitada para comparar com a senha salva
        val hashedPassword = PasswordHasher.hashPassword(password)

        if (user != null && user.password == hashedPassword) {
            _loginResult.value = LoginResult(success = true)
        } else {
            _loginResult.value = LoginResult(success = false, error = "E-mail ou senha inválidos")
        }
    }

    fun insert(user: User) = viewModelScope.launch {
        repository.insert(user)
    }
}

data class LoginResult(
    val success: Boolean,
    val error: String? = null
)

class LoginViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
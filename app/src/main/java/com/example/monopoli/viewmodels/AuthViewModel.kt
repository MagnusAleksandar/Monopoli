package com.example.monopoli.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.monopoli.models.AuthUiState
import com.example.monopoli.repositories.AuthRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {
    object NavigateToRegister : UiEvent()
    private val repository = AuthRepository()
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        checkSession()
    }

    private fun checkSession() {
        val user = repository.getCurrentUser()
        if (user != null) {
            _uiState.value = AuthUiState.Success(user.first, user.second)
        }
    }

    fun login(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _uiState.value = AuthUiState.Error("Campos vacíos")
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading

            val result = repository.login(email, pass)

            if (result.isSuccess) {
                val (uid, userEmail) = result.getOrThrow()
                _uiState.value = AuthUiState.Success(uid, userEmail)
                _eventFlow.emit(UiEvent.NavigateToHome)
            } else {
                _uiState.value = AuthUiState.Error(result.exceptionOrNull()?.message ?: "Error desconocido")
                _eventFlow.emit(UiEvent.ShowSnackbar("Error al iniciar sesión"))
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _uiState.value = AuthUiState.Idle
            _eventFlow.emit(UiEvent.NavigateToLogin)
        }
    }
    fun register(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _uiState.value = AuthUiState.Error("Campos vacíos")
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading

            val result = repository.register(email, pass)

            if (result.isSuccess) {
                val (uid, userEmail) = result.getOrThrow()
                _uiState.value = AuthUiState.Success(uid, userEmail)
                _eventFlow.emit(UiEvent.NavigateToHome)
            } else {
                _uiState.value = AuthUiState.Error(
                    result.exceptionOrNull()?.message ?: "Error desconocido"
                )
                _eventFlow.emit(UiEvent.ShowSnackbar("Error al registrarse"))
            }
        }
    }

    sealed class UiEvent {
        object NavigateToHome : UiEvent()
        object NavigateToLogin : UiEvent()
        object NavigateToRegister : UiEvent()
        data class ShowSnackbar(val message: String) : UiEvent()
    }
}
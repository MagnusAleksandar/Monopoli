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
    private val repository = AuthRepository() //Creamos conexion con el repo

    // Usada en cambios de pantalla, se dibuja automaticamente
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle) //Interno, evita qeu UI modifique el estado
    val uiState = _uiState.asStateFlow()// Solo lectura

    private val _eventFlow = MutableSharedFlow<UiEvent>() //MUestra el Snackbar

    init {
        checkSession()// Se ejcuta al crear el ViewModel
    }

    private fun checkSession() { // Si el usueario ya estaba logeado va directo al home
        val user = repository.getCurrentUser() // pregunta si el usuario ya inicio sesion en el repo
        if (user != null) {
            _uiState.value = AuthUiState.Success(user.first, user.second)
        }
    }

    fun login(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _uiState.value = AuthUiState.Error("Campos vacíos")
            return
        }

        viewModelScope.launch { // Ejecuta un codigo async y se cancela automaticamente si el viewmodel muere
            _uiState.value = AuthUiState.Loading

            val result = repository.login(email, pass) //Llama a firebase y a la funcion loguarsse

            if (result.isSuccess) {
                val (uid, userEmail) = result.getOrThrow() // Obtiene si todo esta bien
                _uiState.value = AuthUiState.Success(uid, userEmail)
                _eventFlow.emit(UiEvent.NavigateToHome) // Como es correcto entonces vamos al siguietne evento que es  HOME
            } else {
                _uiState.value = AuthUiState.Error(result.exceptionOrNull()?.message ?: "Error desconocido")
                _eventFlow.emit(UiEvent.ShowSnackbar("Error al iniciar sesión"))
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout() // Llama la funcion
            _uiState.value = AuthUiState.Idle //Estado inicial
            _eventFlow.emit(UiEvent.NavigateToLogin) //Evente (Login)
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

    //Es como una lista cerrada de oportunidades o eventos
    sealed class UiEvent {
        object NavigateToHome : UiEvent()
        object NavigateToLogin : UiEvent()
        object NavigateToRegister : UiEvent()
        data class ShowSnackbar(val message: String) : UiEvent()
    }
}
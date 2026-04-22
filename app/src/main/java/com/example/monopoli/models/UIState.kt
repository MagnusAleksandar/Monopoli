package com.example.monopoli.models

//Representa el estado de la pantalla
//Usa un sealed class, lo que significa que solo puede usar 4 estados a la vez
sealed class AuthUiState {
    object Idle : AuthUiState() //Sin sesion, estado inicial
    object Loading : AuthUiState() // Esperando respuesta en firebase
    data class Success(val userId: String, val userEmail: String) : AuthUiState() //Ususario autenticado, tiene UserId y email
    data class Error(val message: String) : AuthUiState()// errores
}
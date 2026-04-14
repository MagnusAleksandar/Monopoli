package com.example.monopoli.models

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val userId: String, val userEmail: String) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}
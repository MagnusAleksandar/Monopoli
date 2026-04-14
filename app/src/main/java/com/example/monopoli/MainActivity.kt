package com.example.monopoli

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.monopoli.models.AuthUiState
import com.example.monopoli.ui.theme.Screens.HomeScreen
import com.example.monopoli.ui.theme.Screens.LoginScreen
import com.example.monopoli.ui.theme.Screens.RoomScreen
import com.example.monopoli.ui.theme.MonopoliTheme
import com.example.monopoli.viewmodels.AuthViewModel
import com.example.monopoli.viewmodels.RoomViewModel

class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()
    private val roomViewModel: RoomViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MonopoliTheme {
                // Observamos los estados de los ViewModels para decidir qué pantalla mostrar
                val authUiState by authViewModel.uiState.collectAsState()
                val roomState by roomViewModel.roomState.collectAsState()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val modifier = Modifier.padding(innerPadding)
                    
                    when {
                        // 1. Si hay una sala activa, mostramos la pantalla de la sala
                        roomState != null -> {
                            RoomScreen(roomViewModel)
                        }
                        // 2. Si el usuario está autenticado, mostramos el Home
                        authUiState is AuthUiState.Success -> {
                            HomeScreen(
                                authViewModel = authViewModel,
                                roomViewModel = roomViewModel,
                                modifier = modifier
                            )
                        }
                        // 3. Por defecto (o si no hay sesión), mostramos el Login
                        else -> {
                            LoginScreen(authViewModel)
                        }
                    }
                }
            }
        }
    }
}

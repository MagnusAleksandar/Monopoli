package com.example.monopoli

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.monopoli.models.AuthUiState
import com.example.monopoli.viewmodels.AuthViewModel
import com.example.monopoli.viewmodels.GameViewModel
import com.example.monopoli.viewmodels.RoomViewModel
import com.example.monopoli.ui.theme.Screens.LoginScreen
import com.example.monopoli.ui.theme.Screens.RegisterScreen
import com.example.monopoli.ui.theme.Screens.HomeScreen
import com.example.monopoli.ui.theme.Screens.RoomScreen
import com.example.monopoli.ui.theme.Screens.GameScreen
import com.example.monopoli.ui.theme.Screens.ResultScreen

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val ROOM = "room"
    const val GAME = "game"
    const val RESULT = "result"
}

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel,
    roomViewModel: RoomViewModel,
    gameViewModel: GameViewModel
) {
    val navController = rememberNavController()
    val authState by authViewModel.uiState.collectAsState()
    val roomState by roomViewModel.roomState.collectAsState()

    // Navegación reactiva según el estado de auth
    LaunchedEffect(authState) {
        when (authState) {
            is AuthUiState.Success -> {
                navController.navigate(Routes.HOME) {
                    popUpTo(Routes.LOGIN) { inclusive = true }
                }
            }
            is AuthUiState.Idle -> {
                navController.navigate(Routes.LOGIN) {
                    popUpTo(0) { inclusive = true }
                }
            }
            else -> Unit
        }
    }

    // Navegación reactiva según estado de sala
    LaunchedEffect(roomState) {
        if (roomState != null) {
            navController.navigate(Routes.ROOM) {
                popUpTo(Routes.HOME) { inclusive = false }
            }
        } else {
            // Si sale de la sala, vuelve al home si está autenticado
            if (authState is AuthUiState.Success) {
                navController.navigate(Routes.HOME) {
                    popUpTo(Routes.ROOM) { inclusive = true }
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (authState is AuthUiState.Success) Routes.HOME else Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                viewModel = authViewModel,
                onNavigateToRegister = { navController.navigate(Routes.REGISTER) }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                viewModel = authViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                authViewModel = authViewModel,
                roomViewModel = roomViewModel
            )
        }

        composable(Routes.ROOM) {
            RoomScreen(viewModel = roomViewModel)
        }

        composable(Routes.GAME) {
            GameScreen(gameViewModel = gameViewModel)
        }

        composable(Routes.RESULT) {
            ResultScreen(gameViewModel = gameViewModel)
        }
    }
}
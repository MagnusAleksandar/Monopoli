package com.example.monopoli

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.monopoli.models.AuthUiState
import com.example.monopoli.ui.theme.Screens.GameScreen
import com.example.monopoli.ui.theme.Screens.HomeScreen
import com.example.monopoli.ui.theme.Screens.LoginScreen
import com.example.monopoli.ui.theme.Screens.RegisterScreen
import com.example.monopoli.ui.theme.Screens.ResultScreen
import com.example.monopoli.ui.theme.Screens.RoomScreen
import com.example.monopoli.viewmodels.AuthViewModel
import com.example.monopoli.viewmodels.GameViewModel
import com.example.monopoli.viewmodels.RoomViewModel

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
    gameViewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    val authState by authViewModel.uiState.collectAsState()
    val roomState by roomViewModel.roomState.collectAsState()
    val gameStarted by roomViewModel.gameStarted.collectAsState()

    val currentUserId = (authState as? AuthUiState.Success)?.userId ?: ""

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
            if (authState is AuthUiState.Success) {
                navController.navigate(Routes.HOME) {
                    popUpTo(Routes.ROOM) { inclusive = true }
                }
            }
        }
    }

    // Navegación cuando el host inicia el juego
    LaunchedEffect(gameStarted) {
        if (gameStarted) {
            roomState?.players?.let { players ->
                gameViewModel.initGame(players)
            }
            roomViewModel.resetGameStarted()
            navController.navigate(Routes.GAME) {
                popUpTo(Routes.ROOM) { inclusive = false }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (authState is AuthUiState.Success) Routes.HOME else Routes.LOGIN,
        modifier = modifier
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
            RoomScreen(
                viewModel = roomViewModel,
                gameViewModel = gameViewModel,
                currentUserId = currentUserId
            )
        }

        composable(Routes.GAME) {
            GameScreen(gameViewModel = gameViewModel, currentUserId)
        }

        composable(Routes.RESULT) {
            ResultScreen(gameViewModel = gameViewModel)
        }
    }
}
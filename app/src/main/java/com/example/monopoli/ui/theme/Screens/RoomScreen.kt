package com.example.monopoli.ui.theme.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.monopoli.viewmodels.GameViewModel
import com.example.monopoli.viewmodels.RoomViewModel

@Composable
fun RoomScreen(
    viewModel: RoomViewModel,
    gameViewModel: GameViewModel,
    currentUserId: String
) {
    val room by viewModel.roomState.collectAsState()
    val roomCode by viewModel.roomCode.collectAsState()
    val gameStarted by viewModel.gameStarted.collectAsState()

    // Cuando el juego inicia, inicializar GameViewModel con los jugadores
    LaunchedEffect(gameStarted) {
        if (gameStarted) {
            room?.players?.let { players ->
                gameViewModel.initGame(players)
            }
            viewModel.resetGameStarted()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2E7D32))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "SALA DE ESPERA",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFFD600)
        )

        Spacer(modifier = Modifier.height(20.dp))

        roomCode?.let { code ->
            Text(text = "Código de sala", color = Color.White, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = code,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD600),
                letterSpacing = 8.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Comparte este código con tus amigos",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Estado: ${room?.status ?: "Cargando..."}", color = Color.White)
        Text(text = "Host: ${room?.host ?: "Desconocido"}", color = Color.White)

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Jugadores (${room?.players?.size ?: 0})",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(10.dp))

        room?.players?.values?.forEach { playerName ->
            Card(
                modifier = Modifier.fillMaxWidth(0.8f),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Text(
                    text = playerName,
                    modifier = Modifier.padding(12.dp),
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Botón iniciar — solo visible para el host
        val isHost = room?.host == room?.players?.get(currentUserId)
        if (isHost) {
            Button(
                onClick = { viewModel.startGame() },
                modifier = Modifier.fillMaxWidth(),
                enabled = (room?.players?.size ?: 0) >= 2,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD600))
            ) {
                Text("Iniciar juego", color = Color.Black, fontWeight = FontWeight.Bold)
            }

            if ((room?.players?.size ?: 0) < 2) {
                Text(
                    text = "Necesitas al menos 2 jugadores",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        Button(
            onClick = { viewModel.leaveRoom() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
        ) {
            Text("Salir de la sala", color = Color.White)
        }
    }
}
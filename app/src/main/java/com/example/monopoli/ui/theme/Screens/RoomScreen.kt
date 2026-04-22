package com.example.monopoli.ui.theme.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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

    LaunchedEffect(gameStarted) {
        if (gameStarted) {
            room?.players?.let { players ->
                gameViewModel.initGame(players)
            }
            viewModel.resetGameStarted()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B5E20))
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(10.dp))

            // 🎮 TÍTULO
            Text(
                text = "SALA DE ESPERA",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD600)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 🔢 CARD CÓDIGO
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Black.copy(alpha = 0.3f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text("Código de sala", color = Color.White)

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = roomCode ?: "----",
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD600),
                        letterSpacing = 6.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Compártelo con tus amigos",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ℹ️ INFO
            Text(
                text = "Estado: ${room?.status ?: "..."}",
                color = Color.White
            )
            Text(
                text = "Host: ${room?.host ?: "..."}",
                color = Color.White
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 👥 JUGADORES
            Text(
                text = "Jugadores (${room?.players?.size ?: 0})",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Lista scrollable (importante si hay muchos)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Top
            ) {
                room?.players?.values?.forEach { playerName ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = playerName,
                            modifier = Modifier.padding(14.dp),
                            color = Color.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 🎮 BOTONES
            val isHost = room?.host == room?.players?.get(currentUserId)

            if (isHost) {
                Button(
                    onClick = { viewModel.startGame() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = (room?.players?.size ?: 0) >= 2,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFD600)
                    )
                ) {
                    Text("Iniciar juego", color = Color.Black)
                }

                if ((room?.players?.size ?: 0) < 2) {
                    Text(
                        text = "Mínimo 2 jugadores",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
            }

            Button(
                onClick = { viewModel.leaveRoom() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD32F2F)
                )
            ) {
                Text("Salir", color = Color.White)
            }
        }
    }
}
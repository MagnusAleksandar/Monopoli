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
import com.example.monopoli.viewmodels.RoomViewModel

@Composable
fun RoomScreen(viewModel: RoomViewModel) {

    val room by viewModel.roomState.collectAsState()
    val roomCode by viewModel.roomCode.collectAsState()

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

        // Código de sala
        roomCode?.let { code ->
            Text(
                text = "Código de sala",
                color = Color.White,
                fontSize = 14.sp
            )

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

        Text(
            text = "Estado: ${room?.status ?: "Cargando..."}",
            color = Color.White
        )

        Text(
            text = "Host: ${room?.host ?: "Desconocido"}",
            color = Color.White
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Jugadores",
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

        Button(
            onClick = { viewModel.leaveRoom() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
        ) {
            Text("Salir de la sala", color = Color.White)
        }
    }
}
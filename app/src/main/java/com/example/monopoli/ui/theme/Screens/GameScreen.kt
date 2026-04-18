package com.example.monopoli.ui.theme.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

import com.example.monopoli.viewmodels.GameViewModel

@Composable
fun GameScreen(gameViewModel: GameViewModel) {

    val gameState by gameViewModel.gameState.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1565C0))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // 🎮 TÍTULO
        Text(
            text = "TÍO RICO",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFFD600)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 📊 INFO DEL JUEGO
        Text(
            text = "Turno: ${gameState?.playerNum}",
            color = Color.White
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 👥 JUGADORES
        gameState?.players?.forEach { player ->
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.8f),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Text(
                    text = "${player.name}: $${player.money}",
                    modifier = Modifier.padding(12.dp),
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(30.dp))

        // 🎮 BOTONES
        Button(
            onClick = { gameViewModel.onPlay('u') },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C))
        ) {
            Text("Ahorrar", color = Color.White)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { gameViewModel.onPlay('i') },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
        ) {
            Text("Invertir", color = Color.White)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { gameViewModel.onPlay('d') },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
        ) {
            Text("Gastar", color = Color.White)
        }
    }
}
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
            .background(Color(0xFF0D47A1))
            .systemBarsPadding()
            .navigationBarsPadding()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            "TURNO: ${gameState?.playerNum}",
            color = Color.White
        )

        Spacer(modifier = Modifier.height(10.dp))

        // LISTA
        Column(modifier = Modifier.weight(1f)) {
            gameState?.players?.forEach {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(
                        "${it.name}: $${it.money}",
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }

        // BOTONES
        Button(
            onClick = { gameViewModel.onPlay('u') },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Ahorrar") }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { gameViewModel.onPlay('i') },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Invertir") }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { gameViewModel.onPlay('d') },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) { Text("Gastar") }
    }
}
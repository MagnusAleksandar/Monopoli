package com.example.monopoli.ui.theme.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
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
fun ResultScreen(gameViewModel: GameViewModel) {

    val gameState by gameViewModel.gameState.observeAsState()
    val players = gameState?.players?.sortedByDescending { it.money }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .systemBarsPadding()
            .navigationBarsPadding()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            "RESULTADOS",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Yellow
        )

        Column(modifier = Modifier.weight(1f)) {
            players?.forEachIndexed { index, player ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("#${index + 1} ${player.name}")
                        Text("$${player.money}")
                    }
                }
            }
        }

        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver")
        }
    }
}

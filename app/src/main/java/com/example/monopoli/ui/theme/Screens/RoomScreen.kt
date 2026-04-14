package com.example.monopoli.ui.theme.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.monopoli.viewmodels.RoomViewModel

@Composable
fun RoomScreen(viewModel: RoomViewModel) {

    val room by viewModel.roomState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Sala de espera",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Estado: ${room?.status ?: "Cargando..."}")
        Text(text = "Host: ${room?.host ?: "Desconocido"}")

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Jugadores en la sala:",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        room?.players?.values?.forEach { playerName ->
            Text(
                text = "• $playerName",
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(onClick = { viewModel.leaveRoom() }) {
            Text("Salir de la sala")
        }
    }
}
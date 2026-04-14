package com.example.monopoli.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.monopoli.models.AuthUiState
import com.example.monopoli.viewmodels.AuthViewModel
import com.example.monopoli.viewmodels.RoomViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun HomeScreen(
    authViewModel: AuthViewModel,
    roomViewModel: RoomViewModel,
    modifier: Modifier = Modifier
) {
    val authState by authViewModel.uiState.collectAsState()
    var roomCodeInput by remember { mutableStateOf("") }

    val user = (authState as? AuthUiState.Success)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Bienvenido, ${user?.userEmail ?: "Invitado"}")
        
        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = {
            user?.let {
                roomViewModel.createRoom(it.userId, it.userEmail)
            }
        }) {
            Text("Crear sala")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = roomCodeInput,
            onValueChange = { roomCodeInput = it },
            label = { Text("Código de sala") }
        )

        Button(onClick = {
            user?.let {
                roomViewModel.joinRoom(roomCodeInput, it.userId, it.userEmail)
            }
        }) {
            Text("Unirse a sala")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = {
            authViewModel.logout()
        }) {
            Text("Cerrar sesión")
        }
    }
}

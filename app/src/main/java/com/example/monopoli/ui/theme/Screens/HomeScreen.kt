package com.example.monopoli.ui.theme.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

import com.example.monopoli.models.AuthUiState
import com.example.monopoli.viewmodels.AuthViewModel
import com.example.monopoli.viewmodels.RoomViewModel

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
            .background(Color(0xFF1565C0))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {


        Text(
            text = "TÍO RICO",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFFD600) // amarillo
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Bienvenido, ${user?.userEmail ?: "Invitado"}",
            color = Color.White,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(30.dp))


        Button(
            onClick = {
                user?.let {
                    roomViewModel.createRoom(it.userId, it.userEmail)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C))
        ) {
            Text("Crear sala", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))


        OutlinedTextField(
            value = roomCodeInput,
            onValueChange = { roomCodeInput = it },
            label = { Text("Código de sala") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color.Yellow,
                unfocusedBorderColor = Color.LightGray,
                focusedLabelColor = Color.Yellow,
                unfocusedLabelColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))


        Button(
            onClick = {
                user?.let {
                    roomViewModel.joinRoom(roomCodeInput, it.userId, it.userEmail)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
        ) {
            Text("Unirse a sala", color = Color.White)
        }

        Spacer(modifier = Modifier.height(30.dp))


        Button(
            onClick = {
                authViewModel.logout()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
        ) {
            Text("Cerrar sesión", color = Color.White)
        }
    }
}
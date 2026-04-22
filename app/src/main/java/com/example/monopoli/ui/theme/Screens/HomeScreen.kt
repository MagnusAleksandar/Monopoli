package com.example.monopoli.ui.theme.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
    roomViewModel: RoomViewModel
) {
    val authState by authViewModel.uiState.collectAsState()
    val user = authState as? AuthUiState.Success

    var roomCode by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D47A1))
            .systemBarsPadding()
            .navigationBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // HEADER
        Text(
            "TÍO RICO",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFFD600)
        )

        Text(user?.userEmail ?: "", color = Color.White)

        // CONTENT
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {

            Card(
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Button(
                        onClick = {
                            user?.let {
                                roomViewModel.createRoom(it.userId, it.userEmail)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Crear sala")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = roomCode,
                        onValueChange = { roomCode = it },
                        label = { Text("Código") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            user?.let {
                                roomViewModel.joinRoom(roomCode, it.userId, it.userEmail)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Unirse")
                    }
                }
            }
        }

        // FOOTER
        Button(
            onClick = { authViewModel.logout() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text("Cerrar sesión")
        }
    }
}
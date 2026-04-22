package com.example.monopoli.ui.theme.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.monopoli.R
import com.example.monopoli.models.AuthUiState
import com.example.monopoli.viewmodels.AuthViewModel

@Composable
fun LoginScreen(viewModel: AuthViewModel,onNavigateToRegister: () -> Unit) {

    val uiState by viewModel.uiState.collectAsState() //Enviamos los estados al ViewModel

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) { //Como un snack

        Image(
            painter = painterResource(R.drawable.fondo2),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column( //Layout principal
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "TÍO RICO",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF0000)
            )

            Spacer(modifier = Modifier.height(30.dp))

            TextField(
                value = email,
                onValueChange = { email = it }, //Actualiza email
                label = { Text("E-mail", color = Color(0xFFFF0000)) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    cursorColor = Color(0xFFFF0000),
                    focusedIndicatorColor = Color(0xFFFF0000),
                    unfocusedIndicatorColor = Color.Gray
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password", color = Color(0xFFF60505)) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    cursorColor = Color(0xFFFF0000),
                    focusedIndicatorColor = Color(0xFFFF0000),
                    unfocusedIndicatorColor = Color.Gray,

                ),
                visualTransformation = PasswordVisualTransformation(),//Para que la contraseña no se vea
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = { viewModel.login(email, password) }, //Llamamos al viewmodel, y la funcion login e insertamos los datos
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState !is AuthUiState.Loading, //El boton se desactiva mientras carga
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
            ) {
                Text("Iniciar Sesión", color = Color.White)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onNavigateToRegister,
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState !is AuthUiState.Loading,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
            ) {
                Text("Regístrate", color = Color.White)
            }

            if (uiState is AuthUiState.Error) { // Si el estado es error lo mostramos en pantalla
                Text(
                    text = (uiState as AuthUiState.Error).message,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        if (uiState is AuthUiState.Loading) { // Hacemos una animacion para que se vea el estado de carga
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color(0xFFFF0000),
                    strokeWidth = 4.dp
                )
            }
        }

    }
}



package com.example.monopoli.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.monopoli.R

@Composable
fun LoginScreen(loginClick: (String, String) -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val auth = FirebaseAuth.getInstance()


    Box(modifier = Modifier.fillMaxSize()){

        Image(
            painter = painterResource(R.drawable.fondo2),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ){
            Spacer(modifier = Modifier.height(130.dp))


            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("E-mail", color = Color(0xFFFFD700)) }, // dorado
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color(0xFFFFD700),
                    unfocusedTextColor = Color(0xFFFFD700),
                    cursorColor = Color(0xFFFFD700),
                    focusedIndicatorColor = Color(0xFFFFD700),
                    unfocusedIndicatorColor = Color.Gray
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.padding(22.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password", color = Color(0xFFFFD700)) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color(0xFFFFD700),
                    unfocusedTextColor = Color(0xFFFFD700),
                    cursorColor = Color(0xFFFFD700),
                    focusedIndicatorColor = Color(0xFFFFD700),
                    unfocusedIndicatorColor = Color.Gray
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.padding(110.dp))

            OutlinedButton (onClick = { loginClick(email, password)}) {
                Text("")
            }

        }
    }



}


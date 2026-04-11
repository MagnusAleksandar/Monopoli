package com.example.monopoli.ui.screens

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun HomeScreen(onLogout: () -> Unit) {

    Button(onClick = {
        Firebase.auth.signOut()
        onLogout()
    }) {
        Text("Cerrar sesión")
    }
}
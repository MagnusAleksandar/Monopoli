package com.example.monopoli.view

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
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

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    var isLogged = false
    val unit = {
        isLogged = false // 🔥 VUELVE AL LOGIN
    }
    HomeScreen(unit)
}
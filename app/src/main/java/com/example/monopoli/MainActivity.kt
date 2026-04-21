package com.example.monopoli

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.monopoli.ui.theme.MonopoliTheme
import com.example.monopoli.AppNavigation
import com.example.monopoli.viewmodels.AuthViewModel
import com.example.monopoli.viewmodels.GameViewModel
import com.example.monopoli.viewmodels.RoomViewModel

class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()
    private val roomViewModel: RoomViewModel by viewModels()
    private val gameViewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MonopoliTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(
                        authViewModel = authViewModel,
                        roomViewModel = roomViewModel,
                        gameViewModel = gameViewModel,
                    )
                }
            }
        }
    }
}
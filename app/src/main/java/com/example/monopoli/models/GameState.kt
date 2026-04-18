package com.example.monopoli.models

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

data class GameState(val players: List<Player>, val playerNum: Int = 0){
  val currPlayer get() = players[playerNum]
}


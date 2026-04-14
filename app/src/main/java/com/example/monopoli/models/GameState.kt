package com.example.monopoli.models

data class GameState(val players: List<Player>, val playerNum: Int = 0){
  val currPlayer get() = players[playerNum]
}

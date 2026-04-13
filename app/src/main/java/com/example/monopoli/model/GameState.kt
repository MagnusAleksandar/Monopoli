package com.example.monopoli.model

data class GameState(val players: List<Player>, val playerNum: Int = 0){
  val currPlayer get() = players[playerNum]
}

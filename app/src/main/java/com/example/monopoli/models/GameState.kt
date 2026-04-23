package com.example.monopoli.models

data class GameState(val players: List<Player>,
                     val playerNum: Int = 0,
                     val turns: List<Int> = List(players.size) { 0 }, // turnos por jugador
                     val hostId: Int = 0,
                     val isFinished: Boolean = false){
    val currPlayer get() = players[playerNum]
}
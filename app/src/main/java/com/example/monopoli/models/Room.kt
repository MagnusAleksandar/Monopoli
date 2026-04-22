package com.example.monopoli.models


data class Room(
    val host: String = "",
    val status: String = "",
    val players: Map<String, String> = emptyMap()
){
    fun isGameOver(room: Room, gameState: GameState): Boolean {
        val hostStillInGame = room.players.containsKey(room.host)
        return !hostStillInGame || gameState.isFinished
    }
}


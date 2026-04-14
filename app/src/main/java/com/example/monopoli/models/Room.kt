package com.example.monopoli.models


data class Room(
    val host: String = "",
    val status: String = "",
    val players: Map<String, String> = emptyMap()
)
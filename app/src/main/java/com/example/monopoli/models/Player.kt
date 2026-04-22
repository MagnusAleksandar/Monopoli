package com.example.monopoli.models

data class Player(val id: String, val name: String, var money: Float = 1000.00F, var playing: Boolean = true)
                //ID Nombre,           dinero,                      si está jugando o no (game over individual)

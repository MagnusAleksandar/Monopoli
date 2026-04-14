package com.example.monopoli.models

data class Player(val name: String, var money: Float = 1000.00F, var playing: Boolean = false)
                //Nombre,           dinero,                      si es su turno o no

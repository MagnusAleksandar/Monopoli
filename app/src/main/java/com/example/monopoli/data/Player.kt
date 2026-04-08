package com.example.monopoli.data

data class Player(val name: String, var money: Int = 1000, var playing: Boolean)
                //Nombre,           dinero,         si es su turno o no

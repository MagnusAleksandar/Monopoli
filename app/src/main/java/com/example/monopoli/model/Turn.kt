package com.example.monopoli.model

import com.example.monopoli.data.Player
import kotlin.random.Random

class Turn (private val player: Player, private val interest: Float, private val spent: Float?){
    //      Jugador,            interés del ahorro,  valor gastado
    val possOutcomes = listOf<Char>('g', 'l') // Resultados posibles de invertir (ganar: gain - g, perder: loss - l)

    fun play (currState: GameState, move: String){ // Jugada
        val currPlayers = currState.players.toMutableList()

        when (move){ // El jugador elige una jugada
            'u' -> save()
            'i' -> monUpDown()
            'd' -> spend()
        }

        if (Random.nextBoolean()){ // Evento aleatorio
            monUpDown()

        }

        val nextTurn = (currState.playerNum + 1) % currState.players.size // % hace que vuelva al inicio

        return currState.copy(currPlayers, nextTurn)
        
    }

    fun save(): Player{ // Ahorrar
        var chng = player.money * interest
        return player.copy(money = player.money + chng)
    }

    fun monUpDown(): Player{ // Cambios al monto de dinero del jugador (invertir, eventos aleatorios)
        val outcome = possOutcomes.random()
        val chng = player.money * Random.nextDouble(0.01, 1.00).toFloat()

        when (outcome) {
            'g' -> return player.copy(money = player.money + chng)
            'l' -> return player.copy(money = player.money - chng)
            else -> return player.copy()
        }
    }

    fun spend(): Player{ // Gastar
        return player.copy(money = player.money - (spent ?: 0f))
    }
}

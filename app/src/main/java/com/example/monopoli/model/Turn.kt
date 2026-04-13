package com.example.monopoli.model

import com.example.monopoli.data.Player
import kotlin.random.Random

class Turn (private val player: Player, private val interest: Float, private val spent: Float?){
    //      Jugador,            interés del ahorro,  valor gastado
    val possOutcomes = listOf<Char>('g', 'l') // Resultados posibles de invertir (ganar: gain - g, perder: loss - l)
//    var mon = player.money

    fun save(): Player{ // Ahorrar
        return player.copy(money = player.money * interest)
    }

    fun monUpDown(choice: Char): Player{ // Cambios al monto de dinero del jugador (invertir, eventos aleatorios)
        val outcome = possOutcomes.random()
        val chng: Float

        if (choice != 'y'){
            chng = player.money * Random.nextDouble(0.01, 1.00).toFloat()
        }else{
            chng = player.money * interest
        }

        when (outcome) {
            'g' -> return player.copy(money = player.money + chng)
            'l' -> return player.copy(money = player.money - chng)
            else -> return player.copy()
        }
    }

    fun spend(): Player{ // Gastar
        return player.copy(money = player.money + (spent ?: 0f))
    }
}

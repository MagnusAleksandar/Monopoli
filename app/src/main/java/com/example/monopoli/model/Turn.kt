package com.example.monopoli.model

import com.example.monopoli.data.Player
import kotlin.random.Random

class Turn (val player: Player, val interest: Float, val spent: Float){
    //      Jugador,            interés del ahorro,  valor gastado
    val possOutcomes = listOf<Char>('g', 'l') // Resultados posibles de invertir (ganar: gain - g, perder: loss - l)
    var mon = player.money

    fun save(){
        mon =+ mon * interest

    }

    fun invest(){
        val outcome = possOutcomes.random()
        val chng = Random.nextDouble(0.01, 1.00).toFloat()

        when (outcome){
            'g' -> mon =+ mon * chng
            'l' -> mon =- mon * chng
            else -> print("Acción no válida")

        }

    }

    fun spend(){
        mon =- spent

    }

}
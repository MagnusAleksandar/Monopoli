package com.example.monopoli.models

import kotlin.random.Random

class Turn (private val gameState: GameState, private val interest: Float, private val spent: Float?){
    //      Estado actual del jugador,        interés del ahorro/evento,   valor gastado
    private val player = gameState.currPlayer
    val possOutcomes = listOf<Char>('g', 'l') // Resultados posibles de invertir (ganar: gain - g, perder: loss - l)

    fun play (move: Char): GameState?{ // Jugada

        var newState = when (move){ // El jugador elige una jugada
            'u' -> save()
            'i' -> monUpDown()
            'd' -> spend()
            else -> return null // Jugada inválida, turno finaliza
        }

        if (Random.nextBoolean()){ // Evento aleatorio
            val secondaryTurn = Turn(newState, interest, spent)
            newState = secondaryTurn.monUpDown()
        }

        return newState

    }

    private fun Player.toNewGameState(): GameState {
        val updatedPlayers = gameState.players.toMutableList()

        updatedPlayers[gameState.playerNum] = this // Reemplaza el jugador actual con la versión actualizada después del turno (this = versión actualizada)

        val nextPlayer = (gameState.playerNum + 1) % gameState.players.size // Siguiente jugador (% retorna al primero)

        return gameState.copy(players = updatedPlayers, playerNum = nextPlayer)
    }

    fun save(): GameState{ // Ahorrar
        val chng = player.money * interest // Dinero más interés fijo
        return player.copy(money = player.money + chng).toNewGameState()

    }

    fun monUpDown(): GameState{ // Cambios al monto de dinero del jugador (invertir, eventos aleatorios)
        val outcome = possOutcomes.random()
        val chng = player.money * Random.nextDouble(0.01, 1.00).toFloat()

        val updtPlayer: Player
        when (outcome) {
            'g' -> {
                updtPlayer = player.copy(money = player.money + chng)
                return updtPlayer.toNewGameState()
            }
            'l' -> {
                updtPlayer = player.copy(money = player.money - chng)
                return updtPlayer.toNewGameState()
            }
            else -> return player.copy().toNewGameState()
        }

    }

    fun spend(): GameState{ // Gastar
        return player.copy(money = player.money - (spent ?: 0f)).toNewGameState()
    }
}

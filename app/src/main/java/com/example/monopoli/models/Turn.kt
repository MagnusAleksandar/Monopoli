package com.example.monopoli.models

import kotlin.random.Random

class Turn (private val gameState: GameState, private val interest: Float, private val spent: Float?){
    //      Estado actual del jugador,        interés del ahorro/evento,   valor gastado
    private val player = gameState.currPlayer
    val possOutcomes = listOf<Char>('g', 'l') // Resultados posibles de invertir (ganar: gain - g, perder: loss - l)

    fun play(move: Char): Pair<GameState, Boolean>? {
        var newState = when (move) {
            'u' -> save()
            'i' -> monUpDown()
            'd' -> spend()
            else -> return null
        }

        var eventOccurred = false
        if (Random.nextBoolean()) {
            eventOccurred = true
            val updatedPlayers = newState.players.toMutableList()
            val secondaryTurn = Turn(gameState, interest, spent)
            val eventState = secondaryTurn.monUpDown()
            updatedPlayers[gameState.playerNum] = newState.players[gameState.playerNum]
                .copy(money = eventState.players[gameState.playerNum].money)
            newState = newState.copy(players = updatedPlayers)
        }

        return Pair(newState, eventOccurred)
    }

    private fun Player.toNewGameState(): GameState {
        val updatedPlayers = gameState.players.toMutableList()
        val updatedTurns = gameState.turns.toMutableList()

        // Actualiza jugador
        updatedPlayers[gameState.playerNum] = this

        // Siguiente turno
        updatedTurns[gameState.playerNum] += 1

        // Elimina jugador cuando no tiene dinero
        if (this.money <= 0f) {
            updatedPlayers[gameState.playerNum] =
                this.copy(playing = false)
        }

        // Revisa si todos los jugadores están eliminados
        val gameFinished = updatedPlayers.indices.all { i ->
            updatedTurns[i] >= 5 || !updatedPlayers[i].playing
        }

        // Siguiente jugador
        var nextPlayer = gameState.playerNum
        do {
            nextPlayer = (nextPlayer + 1) % updatedPlayers.size
        } while (!updatedPlayers[nextPlayer].playing && !gameFinished)

        return gameState.copy(
            players = updatedPlayers,
            turns = updatedTurns,
            playerNum = nextPlayer,
            isFinished = gameFinished
        )
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
                val newMoney = maxOf(0f, player.money - chng)
                updtPlayer = player.copy(money = newMoney)
                return updtPlayer.toNewGameState()
            }
            else -> return player.copy().toNewGameState()
        }

    }

    fun spend(): GameState{ // Gastar
        return player.copy(money = player.money - (spent ?: 0f)).toNewGameState()
    }
}

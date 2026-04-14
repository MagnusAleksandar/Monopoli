package com.example.monopoli

import com.example.monopoli.model.GameState
import com.example.monopoli.model.Player
import com.example.monopoli.model.Turn
import org.junit.Test

import org.junit.Assert.*

class TurnUnitTest {
    private val gameState = GameState(listOf(Player("Sam")))
    val INTEREST = 0.30f

    @Test
    fun saveWorks() {
        val turn = Turn(gameState, INTEREST, 5f)
        val result = turn.save()
        assertEquals(1300f, result.currPlayer.money)
    }

    @Test
    fun randomWorks(){
        val turn = Turn(gameState, INTEREST, 5f)
        val result = turn.monUpDown()
        print("Player: ${gameState.currPlayer.name}\n\tResult = ${result.currPlayer.money}, Original = ${gameState.currPlayer.money}\n")
        assert(result.currPlayer.money != gameState.currPlayer.money)
    }

    @Test
    fun investWorks(){
        val turn = Turn(gameState, INTEREST, 5f)
        val result = turn.monUpDown()
//        print("Player: ${player.name}\n\tResult = ${result.money}, Original = ${player.money}\n")
        assert(result.currPlayer.money != gameState.currPlayer.money)
    }

    @Test
    fun spendWorks(){
        val turn = Turn(gameState, INTEREST, 10f)
        val result = turn.spend()
        assertEquals(990f, result.currPlayer.money)
    }
}
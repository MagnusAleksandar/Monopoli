package com.example.monopoli

import com.example.monopoli.data.Player
import com.example.monopoli.model.Turn
import org.junit.Test

import org.junit.Assert.*

class TurnUnitTest {
    private val player = Player("Sam")
    val INTEREST = 0.30f

    @Test
    fun saveWorks() {
        val turn = Turn(player, INTEREST, 5f)
        val result = turn.save()
        assertEquals(1300f, result.money)
    }

    @Test
    fun randomWorks(){
        val turn = Turn(player, INTEREST, 5f)
        val result = turn.monUpDown()
        print("Player: ${player.name}\n\tResult = ${result.money}, Original = ${player.money}\n")
        assert(result.money != player.money)
    }

    @Test
    fun investWorks(){
        val turn = Turn(player, INTEREST, 5f)
        val result = turn.monUpDown()
//        print("Player: ${player.name}\n\tResult = ${result.money}, Original = ${player.money}\n")
        assert(result.money != player.money)
    }

    @Test
    fun spendWorks(){
        val turn = Turn(player, INTEREST, 10f)
        val result = turn.spend()
        assert(result.money < player.money)
    }
}
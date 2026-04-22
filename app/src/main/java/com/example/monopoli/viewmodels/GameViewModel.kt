package com.example.monopoli.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.monopoli.models.GameState
import com.example.monopoli.models.Player
import com.example.monopoli.models.Turn

    //Recibe jugadores de la sala
    class GameViewModel : ViewModel() {

        private val INTEREST = .15f

        private val _gameState = MutableLiveData<GameState>()
        val gameState: LiveData<GameState> = _gameState

        fun initGame(players: Map<String, String>) {
            val playerList = players.values.map { Player(it) }
            _gameState.value = GameState(playerList)
        }

        private fun turn() = Turn(_gameState.value!!, INTEREST, null)

        fun onPlay(choice: Char) {
            _gameState.value = turn().play(choice)
        }
    }


package com.example.monopoli.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.monopoli.models.GameState
import com.example.monopoli.models.Player
import com.example.monopoli.models.Turn
import com.example.monopoli.repositories.RoomRepository

//Recibe jugadores de la sala
    class GameViewModel : ViewModel() {
        lateinit var roomCode: String
        private val roomRepository = RoomRepository()

        private val INTEREST = .15f

        private val _gameState = MutableLiveData<GameState>()
        val gameState: LiveData<GameState> = _gameState

        fun initGame(players: Map<String, String>) {
            val playerList = players.map { (id, name) ->
                Player(name = name, id = id)
            }
            _gameState.value = GameState(playerList)
        }

        private fun turn() = Turn(_gameState.value!!, INTEREST, null)

        fun onPlay(choice: Char, playerID: String?) {
            val currentState = _gameState.value ?: return

            val currentPlayer = currentState.currPlayer
            if (!currentState.currPlayer.playing) return

            // No puede jugar si no es su turno
            if (currentPlayer.name != playerID) return

            // No puede jugar si se acabó el juego
            if (currentState.isFinished) return
            val newState = turn().play(choice)?: return
            _gameState.value = newState

            if(newState.isFinished){
                roomRepository.endGame(roomCode)
            } else {
                roomRepository.updateGameState(roomCode, newState)
            }

        }
    }


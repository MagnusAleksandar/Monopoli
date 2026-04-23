package com.example.monopoli.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.monopoli.models.GameState
import com.example.monopoli.models.Player
import com.example.monopoli.models.Turn
import com.example.monopoli.repositories.RoomRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

//Recibe jugadores de la sala
class GameViewModel : ViewModel() {
    private val _randomEventOccurred = MutableStateFlow(false)
    val randomEventOccurred: StateFlow<Boolean> = _randomEventOccurred

    lateinit var roomCode: String
    private val roomRepository = RoomRepository()

    private val INTEREST = .15f

    private val _gameState = MutableLiveData<GameState>()
    val gameState: LiveData<GameState> = _gameState

    private var gameInitialized = false

    fun initGame(players: Map<String, String>) {
        val playerList = players.map { (id, name) ->
            Player(name = name, id = id, playing = true)
        }
        _gameState.value = GameState(playerList)
        roomRepository.listenGameState(roomCode) { newState ->
            if (gameInitialized) {
                _gameState.postValue(newState)
            } else {
                gameInitialized = true
            }
        }
    }

    private fun turn() = Turn(_gameState.value!!, INTEREST, null)

    fun onPlay(choice: Char, playerID: String?) {
        val currentState = _gameState.value ?: return
        val currentPlayer = currentState.currPlayer
        if (!currentPlayer.playing) return
        if (currentPlayer.id != playerID) return
        if (currentState.isFinished) return

        val result = turn().play(choice) ?: return
        val (newState, eventOccurred) = result
        _randomEventOccurred.value = eventOccurred

        if (newState.isFinished) {
            roomRepository.deleteRoom(roomCode)
        } else {
            roomRepository.updateGameState(roomCode, newState)
        }
    }
}

package com.example.monopoli.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.monopoli.models.Room
import com.example.monopoli.repositories.RoomRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RoomViewModel : ViewModel() {

    private val repository = RoomRepository()

    private val _roomState = MutableStateFlow<Room?>(null)
    val roomState = _roomState.asStateFlow()

    private val _roomCode = MutableStateFlow<String?>(null)
    val roomCode = _roomCode.asStateFlow()

    private val _joinError = MutableStateFlow<String?>(null)
    val joinError = _joinError.asStateFlow()

    private val _gameStarted = MutableStateFlow(false)
    val gameStarted = _gameStarted.asStateFlow()

    fun createRoom(userId: String, userName: String) {
        val code = generateRoomCode()
        _roomCode.value = code
        repository.createRoom(code, userId, userName)
        listenRoom(code)
    }

    fun joinRoom(code: String, userId: String, userName: String) {
        viewModelScope.launch {
            val result = repository.joinRoom(code, userId, userName)
            if (result.isSuccess) {
                _roomCode.value = code
                _joinError.value = null
                listenRoom(code)
            } else {
                _joinError.value = result.exceptionOrNull()?.message ?: "Error al unirse"
            }
        }
    }

    // ✅ Una sola versión con toda la lógica
    private fun listenRoom(code: String) {
        repository.listenRoom(code) { room ->
            _roomState.value = room

            val hostStillExists = room.players.containsValue(room.host) // si el host se va, acaba el juego
            if (!hostStillExists) {
                _gameStarted.value = false
                return@listenRoom
            }

            if (room.status == "playing") {
                _gameStarted.value = true
            }
        }
    }

    fun startGame() {
        val code = _roomCode.value ?: return
        repository.startGame(code)
    }

    fun resetGameStarted() {
        _gameStarted.value = false
    }

    private fun generateRoomCode(): String {
        return (100000..999999).random().toString()
    }

    fun leaveRoom() {
        _roomState.value = null
        _roomCode.value = null
        _joinError.value = null
        _gameStarted.value = false
    }

    fun clearJoinError() {
        _joinError.value = null
    }
}
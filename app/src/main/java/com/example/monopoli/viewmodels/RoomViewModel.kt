package com.example.monopoli.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.monopoli.models.Room
import com.example.monopoli.repositories.RoomRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RoomViewModel : ViewModel() {

    private val repository = RoomRepository() // Insertamos lo del firebase aca

    private val _roomState = MutableStateFlow<Room?>(null) //Manejamos el estado de la sala
    val roomState = _roomState.asStateFlow()

    private val _roomCode = MutableStateFlow<String?>(null)
    val roomCode = _roomCode.asStateFlow()// Guardamos el codigo de la sala

    private val _joinError = MutableStateFlow<String?>(null)
    val joinError = _joinError.asStateFlow()

    private val _gameStarted = MutableStateFlow(false) // Estado del juego, si ya inicio o no
    val gameStarted = _gameStarted.asStateFlow()
    private var currentUserId: String = "" // Guardamos usuario

    fun createRoom(userId: String, userName: String) {
        currentUserId = userId //guardamos el UserID para ver quien asle y quien entra
        val code = generateRoomCode()
        _roomCode.value = code // Cogemos el codigo de l sala
        repository.createRoom(code, userId, userName) //Funcion de crear la sala
        listenRoom(code) // La llamamos apra que la sala cambie a el estado "playing"
    }

    fun joinRoom(code: String, userId: String, userName: String) {
        currentUserId = userId
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

    //  Una sola versión con toda la lógica
    private fun listenRoom(code: String) {
        repository.listenRoom(code) { room ->
            _roomState.value = room
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
        val code = _roomCode.value
        val room = _roomState.value

        if (code != null && currentUserId.isNotEmpty()) {
            // Si es el host elimina toda la sala, si no solo se elimina a sí mismo
            if (room?.players?.get(currentUserId) == room?.host) {
                repository.deleteRoom(code)
            } else {
                repository.removePlayer(code, currentUserId)
            }
        }

        repository.stopListener()
        _roomState.value = null
        _roomCode.value = null
        _joinError.value = null
        _gameStarted.value = false
        currentUserId = ""
    }


    fun clearJoinError() {
        _joinError.value = null
    }
}
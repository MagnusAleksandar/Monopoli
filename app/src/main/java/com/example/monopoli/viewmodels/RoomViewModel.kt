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

    fun createRoom(userId: String, userName: String) {
        val code = generateRoomCode()
        repository.createRoom(code, userId, userName)
        listenRoom(code)
    }

    fun joinRoom(code: String, userId: String, userName: String) {
        viewModelScope.launch {
            val result = repository.joinRoom(code, userId, userName)
            if (result.isSuccess) {
                listenRoom(code)
            }
        }
    }

    private fun listenRoom(code: String) {
        repository.listenRoom(code) {
            _roomState.value = it
        }
    }

    private fun generateRoomCode(): String {
        return (100000..999999).random().toString()
    }

    fun leaveRoom() {
        _roomState.value = null
    }
}
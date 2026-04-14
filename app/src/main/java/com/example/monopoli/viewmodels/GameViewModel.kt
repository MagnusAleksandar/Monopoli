package com.example.monopoli.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.monopoli.models.GameState
import com.example.monopoli.models.Player
import com.example.monopoli.models.Turn

class GameViewModel: ViewModel(){
    private val INTEREST = .15f
    private val _gameState = MutableLiveData<GameState>(
        GameState(listOf(Player("Player1"), Player("Player2")))
    )
    val gameState: LiveData<GameState> = _gameState

    private fun turn(inc: Float? = null) = Turn(_gameState.value!!, INTEREST, inc)

    fun onPlay(choice: Char) {
        _gameState.value = turn().play( choice)
    }

}

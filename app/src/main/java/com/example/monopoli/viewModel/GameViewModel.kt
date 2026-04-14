package com.example.monopoli.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.monopoli.model.GameState
import com.example.monopoli.model.Player
import com.example.monopoli.model.Turn

class GameViewModel: ViewModel(){
    private val INTEREST = .15f
    private val _gameState = MutableLiveData<GameState>(
        GameState(listOf(Player("Player1"), Player("Player2")))
    )
    val gameState: LiveData<GameState> = _gameState

    private fun turn(inc: Float? = null) = Turn(_gameState.value!!, INTEREST, inc)

    fun onSave() {
        _gameState.value = turn().save()
    }

    fun onInvest(){
        _gameState.value = turn().monUpDown()
    }

    fun onSpend(inc: Float){
        _gameState.value = turn(inc).spend()
    }

    fun onRandom(){
        _gameState.value = turn().monUpDown()
    }

}

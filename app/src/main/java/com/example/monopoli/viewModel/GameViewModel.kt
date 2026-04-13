package com.example.monopoli.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.monopoli.data.Player
import com.example.monopoli.model.Turn

class GameViewModel: ViewModel(){
    private val INTEREST = .15f
    private val _player = MutableLiveData<Player>(Player("Player"))
    val player: LiveData<Player> = _player

    private fun turn(inc: Float? = null) = Turn(_player.value!!, INTEREST, inc)

    fun onSave(){
        val newState = turn.save()
        _gameState.value = newState
    }

    fun onInvest(){
        _player.value = turn().monUpDown()
    }

    fun onSpend(inc: Float){
        _player.value = turn(inc).spend()
    }

    fun onRandom(){
        _player.value = turn().monUpDown()
    }

}

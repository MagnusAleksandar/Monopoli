package com.example.monopoli.model

import com.example.monopoli.data.Player

class Turn (val player: Player, val interest: Float){
    var mon = player.money

    fun save(){
        mon = mon + mon * interest

    }

}
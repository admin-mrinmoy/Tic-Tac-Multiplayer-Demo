package com.mrinmoy.tictacgo

import kotlin.random.Random

data class GameModel(
    var gameID:String="-1",
    var filledPosition:MutableList<String> = mutableListOf("","","","","","","","",""),
    var winner:String="",
    var gameStatus:GameStatus=GameStatus.CREATED,
    var currentPlayer:String= (arrayOf("X","O"))[Random.nextInt(2)]
)
enum class GameStatus{
    CREATED,
    JOINED,
    INPROGRESSED,
    FINISHED
}

package com.bignerdranch.android.pennydrop.game

import com.bignerdranch.android.pennydrop.types.Player

//game status
/**
 * @property coinChangeCount - quantity of the filled penny
 */
data class TurnResult(
    val lastRoll: Int? =  null,
    val coinChangeCount: Int? = null,
    val previousPlayer: Player? = null,
    val currentPlayer: Player? = null,
    val playerChanged: Boolean = false,
    val turnEnd: TurnEnd? = null,
    val canRoll: Boolean = false,
    val canPass: Boolean = false,
    val clearSlots: Boolean = false,
    val isGameOver: Boolean = false
)

//variants of finishing the game
//Bust - user takes all the penny from dask
enum class TurnEnd { Pass, Bust, Win}

package com.bignerdranch.android.pennydrop.types

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.bignerdranch.android.pennydrop.game.AI

//Room creates table for database with annotation @Entity
//@Entity класс определяет структкру таблицы
/**
 * PrimaryKey (autoGenerate = true) var playerId: Long - let Room calculate the next
 * gameId value by itself
 * @param selectedAI chosen AI
 */
@Entity(tableName ="players")
data class Player(
    @PrimaryKey(autoGenerate = true) var playerId: Long= 0,
    val playerName: String="",
    val isHuman: Boolean = true,
    val selectedAI: AI? = null
) {

    //Ignores the marked element from Room's processing logic,
    // you can add it to a field of an Entity and Room will not persist that field.
    @Ignore
    var pennies: Int = defaultPennyCount

    //Ignores the marked element from Room's processing logic,
    // you can add it to a field of an Entity and Room will not persist that field.
    @Ignore
    var isRolling : Boolean = false

    //Ignores the marked element from Room's processing logic,
    // you can add it to a field of an Entity and Room will not persist that field.
    @Ignore
    var gamePlayingNumber: Int = -1

    /**
     * check player's quantity of penny and subtract 1 penny
     */
    fun penniesLeft(subtractPenny: Boolean = false) =
        (pennies - (if (subtractPenny) 1 else 0)) > 0

    companion object {
        const val defaultPennyCount = 10
    }
}
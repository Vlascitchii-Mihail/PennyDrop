package com.bignerdranch.android.pennydrop.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.OffsetDateTime

//Room creates table for database with annotation @Entity
//@Entity класс определяет структкру таблицы
@Entity(tableName = "games")
/**
 * @since @PrimaryKey (autoGenerate = true) var gameId: Long - let Room calculate the next
 * gameId value by itself
 */
//All the information about the game
data class Game (
    @PrimaryKey (autoGenerate = true) var gameId: Long = 0,
    val gameState: GameState = GameState.Started,
    val startTime: OffsetDateTime? = OffsetDateTime.now(),
    val endTime: OffsetDateTime? = null,
    val filledSlots: List<Int> = emptyList(),
    val lastRoll: Int? = null,
    val currentTurnText: String? = null,
    val canRoll: Boolean = false,
    val canPass: Boolean = false
)
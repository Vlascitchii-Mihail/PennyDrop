package com.bignerdranch.android.pennydrop.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.bignerdranch.android.pennydrop.types.Player

//@Dao - абстрактный кдасс доступа к базе данных
@Dao

abstract class PennyDropDao {
    /**
     * @param :playerName - send the parameter to the Query
     */
    @Query("SELECT * FROM players WHERE playerName = :playerName")

    abstract fun getPlayer(playerName: String): Player?

    @Insert
    abstract suspend fun insertGame(game: Game): Long

    @Insert
    abstract suspend fun insertPlayer(player: Player): Long

    @Insert
    abstract suspend fun insertPlayers(players: List<Player>): List<Long>

    @Update
    abstract suspend fun updateGame(game: Game)
}
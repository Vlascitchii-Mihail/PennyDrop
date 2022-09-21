package com.bignerdranch.android.pennydrop.data

import com.bignerdranch.android.pennydrop.types.Player

class PennyDropRepository(private val pennyDropDao: PennyDropDao) {

    fun getCurrentGameWithPlayers() = pennyDropDao.getCurrentGameWithPlayers()

    fun getCurrentGameStatuses() = pennyDropDao.getCurrentGameStatuses()

    suspend fun startGame(players: List<Player>) = pennyDropDao.startGame(players)

    suspend fun updateGameAndStatuses(game: Game, statuses: List<GameStatus>) =
        pennyDropDao.updateGameAndStatuses(game, statuses)

    companion object {
        @Volatile
        private var instance: PennyDropRepository? = null

        fun getInstance(pennyDropDao: PennyDropDao) =
            this.instance ?: synchronized(this) {
                instance ?: PennyDropRepository(pennyDropDao).also {
                    instance = it
                }
            }
    }
}
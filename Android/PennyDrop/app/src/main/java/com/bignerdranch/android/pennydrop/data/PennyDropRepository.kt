package com.bignerdranch.android.pennydrop.data

import androidx.lifecycle.LiveData
import com.bignerdranch.android.pennydrop.types.Player

/**
 * create a new Repository's object
 */
class PennyDropRepository(private val pennyDropDao: PennyDropDao) {

    fun getCurrentGameWithPlayers() = pennyDropDao.getCurrentGameWithPlayers()

    fun getCurrentGameStatuses() = pennyDropDao.getCurrentGameStatuses()

    /**
     * @param pennyCount - default penny value
     */
    suspend fun startGame(players: List<Player>, pennyCount: Int? = null) =
        pennyDropDao.startGame(players, pennyCount)

    suspend fun updateGameAndStatuses(game: Game, statuses: List<GameStatus>) =
        pennyDropDao.updateGameAndStatuses(game, statuses)

    fun getCompletedGameStatusesWithPlayers(): LiveData<List<GameStatusWithPlayer>> =
        pennyDropDao.getCompletedGameStatusesWithPlayers()

    /**
     * @property instance refers to the repository
     */
    companion object {
        @Volatile
        private var instance: PennyDropRepository? = null

        /**
         * create a new Repository's object
         */
        fun getInstance(pennyDropDao: PennyDropDao) =
            this.instance ?: synchronized(this) {
                instance ?: PennyDropRepository(pennyDropDao).also {
                    instance = it
                }
            }
    }
}
package com.bignerdranch.android.pennydrop.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.bignerdranch.android.pennydrop.data.PennyDropDao_Impl
import com.bignerdranch.android.pennydrop.data.PennyDropDatabase
import com.bignerdranch.android.pennydrop.data.PennyDropRepository
import com.bignerdranch.android.pennydrop.types.PlayerSummary

class RankingsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PennyDropRepository

    val playerSummaries: LiveData<List<PlayerSummary>>

    init {

        //get rhe repository's object
        this.repository = PennyDropDatabase.getDatabase(application, viewModelScope)
            .pennyDropDao().let { dao ->
                PennyDropRepository.getInstance(dao)
            }

        //convert List<GameStatusWithPlayer> to List<PlayerSummary>
        playerSummaries = Transformations.map(this.repository.getCompletedGameStatusesWithPlayers()) { statusesWithPlayers ->

            //groupBy() - Groups elements of the original collection by the key returned by the given keySelector function
            // applied to each element and returns a map where each group key is associated with a list of corresponding elements
            statusesWithPlayers.groupBy { it.player }
                .map { (player, statuses) ->
                PlayerSummary(
                    player.playerId,
                    player.playerName,
                    statuses.count(),
                    statuses.count { it.gameStatus.pennies == 0},
                    player.isHuman)

                    // -  -> Сортировка по убыванию.
                }.sortedWith(compareBy({ -it.wins }, { -it.gamesPlayed}))
        }
    }
}
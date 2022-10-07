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
        this.repository = PennyDropDatabase.getDatabase(application, viewModelScope)
            .pennyDropDao().let { dao ->
                PennyDropRepository.getInstance(dao)
            }

        playerSummaries = Transformations.map(this.repository.getCompletedGameStatusesWithPlayers()) { statusesWithPlayers ->

        }
    }
}
package com.bignerdranch.android.pennydrop.viewmodels

import androidx.lifecycle.ViewModel
import com.bignerdranch.android.pennydrop.types.Player

class GameViewModel: ViewModel() {
    private var players: List<Player> = emptyList()

    fun startGame(playersForNewGame: List<Player>) {
        this.players = playersForNewGame
    }
}
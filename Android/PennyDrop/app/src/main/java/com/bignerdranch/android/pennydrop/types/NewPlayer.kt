package com.bignerdranch.android.pennydrop.types

import androidx.databinding.ObservableBoolean
import com.bignerdranch.android.pennydrop.game.AI

//describes a player
data class NewPlayer(
    var playerName: String="",
    val isHuman : ObservableBoolean = ObservableBoolean(true),
    val canBeRemoved : Boolean = true,
    val canBeToggled: Boolean = true,
    var isIncluded: ObservableBoolean = ObservableBoolean(!canBeRemoved),
    var selectedAIPerson: Int = -1
) {

    //returns current AI number
    fun selectedAi() = if(!isHuman.get()) {
        AI.basicAI.getOrNull(selectedAIPerson)
    } else null

    //transform newPlayer's object to Player's object
    fun toPlayer() = Player(
        if (this.isHuman.get()) {
            this.playerName
        } else (this.selectedAi()?.name ?: "AI"),
        this.isHuman.get(),
        this.selectedAi()
    )
}
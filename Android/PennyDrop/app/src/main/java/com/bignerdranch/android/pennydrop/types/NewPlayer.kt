package com.bignerdranch.android.pennydrop.types

import androidx.databinding.ObservableBoolean
import com.bignerdranch.android.pennydrop.game.AI

/**
 *  information about new players, player's lobby
 *  @param selectedAIPerson AI's index
 *  @param isIncluded player is included in game or not
 *  @param canBeToggled the player can be included in game or not
 */
data class NewPlayer(
    var playerName: String="",
    val isHuman : ObservableBoolean = ObservableBoolean(true),
    val canBeRemoved : Boolean = true,
    val canBeToggled: Boolean = true,
    var isIncluded: ObservableBoolean = ObservableBoolean(!canBeRemoved),
    var selectedAIPerson: Int = -1
) {

    /**
     * @return returns current AI number
     */
    fun selectedAi() = if(!isHuman.get()) {
        AI.basicAI.getOrNull(selectedAIPerson)
    } else null

    /**
     * transform newPlayer's object to Player's object
     * @return a new Player
     */
    fun toPlayer() = Player(
        playerName = if (this.isHuman.get()) {
            this.playerName
        } else (this.selectedAi()?.name ?: "AI"),
        isHuman = this.isHuman.get(),
        selectedAI = this.selectedAi()
    )
}
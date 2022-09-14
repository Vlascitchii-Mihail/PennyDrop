package com.bignerdranch.android.pennydrop.types

import com.bignerdranch.android.pennydrop.game.AI

class Player(
    val playerName: String="",
    val isHuman: Boolean = true,
    val selectedAI: AI? = null
) {
    var pennies: Int = defaultPennyCount
    var isRolling : Boolean = false

    fun addPennies(count : Int = 1) {
        pennies += count
    }

    companion object {
        const val defaultPennyCount = 10
    }
}
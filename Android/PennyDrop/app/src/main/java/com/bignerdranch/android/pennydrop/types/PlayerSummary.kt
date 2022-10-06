package com.bignerdranch.android.pennydrop.types

/**
 * Ranking holder for ViewHolder
 */

class PlayerSummary(
    val id: Long,
    val name: String,
    val gamesPlayed: Int = 0,
    val wins: Int = 0,
    val winsString: String = wins.toString(),
    val isHuman: Boolean = true
) {
}
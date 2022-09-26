package com.bignerdranch.android.pennydrop.types

import androidx.lifecycle.MutableLiveData
import com.bignerdranch.android.pennydrop.data.Game
import kotlin.random.Random

/**
 * @param number - number of the slot
 */
data class Slot(

    val number: Int,
    val canBeFilled: Boolean = true,
    var isFilled: Boolean = false,
    var lastRolled: Boolean = false
) {
    companion object {
        fun mapFromGame(game: Game?) =
            (1..6).map { slotNum ->
                Slot(
                    number = slotNum,
                    canBeFilled = slotNum != 6,
                    isFilled = game?.filledSlots?.contains(slotNum) ?: false,
                    lastRolled = game?.lastRoll == slotNum
                )
            }
    }
}

/**
 * @since clear() - clean the fields isFilled and lastRolled of the Slot,
 * doesn't send to the LiveData listeners any notifications
 */
fun List<Slot>.clear() = this.forEach { slot ->
    slot.isFilled = false
    slot.lastRolled = false
}

/**
 * @since __.fullSlots() - show quantity of full slots
 */
fun List<Slot>.fullSlots(): Int =
    this.count {it.canBeFilled && it.isFilled}

/**
 * @since coinFlipIsHeads() - returns random Boolean
 */
fun coinFlipIsHeads() = Random.nextBoolean()


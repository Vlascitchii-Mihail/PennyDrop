package com.bignerdranch.android.pennydrop.game

import com.bignerdranch.android.pennydrop.types.Player
import com.bignerdranch.android.pennydrop.types.Slot
import com.bignerdranch.android.pennydrop.types.coinFlipIsHeads
import com.bignerdranch.android.pennydrop.types.fullSlots

class AI(val name: String,
         /**
          * @since rollAgain() - AI's decision to roll or not after first rilling
          */
         val rollAgain: (slots: List<Slot>) -> Boolean) {
    override fun toString() = name

    /**
     * @since toPlayer() - recycle AI to Player
     */
    fun toPlayer() = Player(
        playerName = name,
        isHuman = false,
        selectedAI = this
    )

    companion object {

        //@JvmStatic - command to create an additional static method get() for basicAI
        //and allows to use basicAI in player_list_item.xml
        @JvmStatic
        val basicAI = listOf(
            AI("TwoFace") { slots -> slots.fullSlots() < 3 || (slots.fullSlots() == 3 && coinFlipIsHeads()) },
            AI("No Go Noah") { slots -> slots.fullSlots() == 0 },
            AI("Bail Out Beulah") { slots -> slots.fullSlots() <= 1 },
            AI("Fearful Fred") { slots -> slots.fullSlots() <= 2 },
            AI("Even Steven") { slots -> slots.fullSlots() <= 3 },
            AI("Riverboat Ron") { slots -> slots.fullSlots() <= 4 },
            AI("Sammy Sixes") { slots -> slots.fullSlots() <= 5 },
            AI("Random Rachael") { coinFlipIsHeads() }
        )
    }
}
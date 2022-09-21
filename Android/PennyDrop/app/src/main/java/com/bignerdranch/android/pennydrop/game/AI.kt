package com.bignerdranch.android.pennydrop.game

import com.bignerdranch.android.pennydrop.types.Player
import com.bignerdranch.android.pennydrop.types.Slot
import com.bignerdranch.android.pennydrop.types.coinFlipIsHeads
import com.bignerdranch.android.pennydrop.types.fullSlots

class AI(val name: String,
         val aiId: Long = 0,
         /**
          * @since rollAgain() - AI's decision to roll or not after first rilling
          */
         val rollAgain: (slots: List<Slot>) -> Boolean) {
    override fun toString() = name

    /**
     * @since toPlayer() - recycle AI to Player
     */
    fun toPlayer() = Player(
        playerId = aiId,
        playerName = name,
        isHuman = false,
        selectedAI = this
    )

    companion object {

        //@JvmStatic - command to create an additional static method get() for basicAI
        //and allows to use basicAI in player_list_item.xml
        @JvmStatic
        val basicAI = listOf(
            AI("TwoFace",1) { slots -> slots.fullSlots() < 3 || (slots.fullSlots() == 3 && coinFlipIsHeads()) },
            AI("No Go Noah",2) { slots -> slots.fullSlots() == 0 },
            AI("Bail Out Beulah",3) { slots -> slots.fullSlots() <= 1 },
            AI("Fearful Fred",4) { slots -> slots.fullSlots() <= 2 },
            AI("Even Steven",5) { slots -> slots.fullSlots() <= 3 },
            AI("Riverboat Ron",6) { slots -> slots.fullSlots() <= 4 },
            AI("Sammy Sixes",7) { slots -> slots.fullSlots() <= 5 },
            AI("Random Rachael",8) { coinFlipIsHeads() }
        )
    }
}
package com.bignerdranch.android.pennydrop.game

class AI(val name: String) {
    override fun toString() = name

    companion object {

        //@JvmStatic - command to create an additional static method get() for basicAI
        //and allows to use basicAI in payer_list_item.xml
        @JvmStatic
        val basicAI = listOf(AI("TwoFace"), AI("No Go Noah"),
        AI("BAil Out Beulah"), AI("Fearful Fred"), AI("Even Steven"),
        AI("Riverboat Ron"), AI("Sammy Sixes"), AI("Random Rachel"))
    }
}
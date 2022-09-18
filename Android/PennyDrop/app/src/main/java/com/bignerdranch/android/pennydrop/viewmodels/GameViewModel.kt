package com.bignerdranch.android.pennydrop.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bignerdranch.android.pennydrop.game.GameHandler
import com.bignerdranch.android.pennydrop.game.TurnEnd
import com.bignerdranch.android.pennydrop.game.TurnResult
import com.bignerdranch.android.pennydrop.types.Player
import com.bignerdranch.android.pennydrop.types.Slot
import com.bignerdranch.android.pennydrop.types.clear
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @property currentPlayer - current player in the game
 * @property canRoll - opportunity to the current player to make a move
 * @property canPass - opportunity to the current player to make a pass
 * @property currentTurnText - game's history
 * @property currentStandingsText - result of the game
 */
class GameViewModel: ViewModel() {
    private var players: List<Player> = emptyList()

    val slots = MutableLiveData(
        (1..6).map { slotNum -> Slot(slotNum, slotNum != 6) }
    )

    //current player in the game
    val currentPlayer = MutableLiveData<Player?>()

    //opportunity to the current player to make a move
    val canRoll = MutableLiveData(false)

    //opportunity to the current player to make a pass
    val canPass = MutableLiveData(false)

    //game's history
    val currentTurnText = MutableLiveData("")

    //result of the game
    var currentStandingsText = MutableLiveData("")

    var clearText = false

    fun startGame(playersForNewGame: List<Player>) {

        //getting the players fom PickPlayersViewModel
        this.players = playersForNewGame
        this.currentPlayer.value = this.players.firstOrNull().apply {
            this?.isRolling = true
        }

        this.canRoll.value = true
        this.canPass.value = false

        slots.value?.clear()
        slots.notifyChange()

        currentTurnText.value = "The game has begun!\n"
        currentStandingsText.value = generateCurrentStandings(this.players)
    }

    /**
     * @since roll() - fun is called from fragment_game.xml, calls anther fun roll() from GameHandler
     */
    fun roll() {
        slots.value?.let { currentSlots ->

            //Comparing against true saves us a null check
            val currentPlayer = players.firstOrNull { it.isRolling }
            if (currentPlayer != null && canRoll.value == true) {
                updateFromGameHandler(GameHandler.roll(players, currentPlayer, currentSlots))
            }
        }
    }

    /**
     * @since pass() - fun is coled from fragment_game.xml, calls anther fun pass() from GameHandler
     */
    fun pass() {
        val currentPlayer = players.firstOrNull { it.isRolling }
        if (currentPlayer != null && canPass.value == true) {
            updateFromGameHandler(GameHandler.pass(players, currentPlayer))
        }
    }

    /**
     * @since notifyChange update LiveData with the same value for sending this data to LiveData listeners
     */
    private fun <T> MutableLiveData<List<T>>.notifyChange() {
        this.value = this.value
    }

    /**
     * @since updateFromGameHandler() - update the UI of the fragment_game.xml
     */
    private  fun updateFromGameHandler(result: TurnResult) {
        if (result.currentPlayer != null) {
            currentPlayer.value?.addPennies(result.coinChangeCount ?: 0)
            currentPlayer.value = result.currentPlayer
            this.players.forEach { player ->
                player.isRolling = result.currentPlayer == player
            }
        }

        if (result.lastRoll != null) {
            slots.value?.let { currentSlots ->
                updateSlots(result, currentSlots, result.lastRoll)
            }
        }

        currentTurnText.value = generateTurnText(result)
        currentStandingsText.value = generateCurrentStandings(this.players)

        canRoll.value = result.canRoll
        canPass.value = result.canPass

        if (!result.isGameOver && result.currentPlayer?.isHuman == false) {
            canPass.value = false
            canRoll.value = false

            //AI's turn to play
            playAITurn()
        }
    }

    /**
     * @since updateSlots() - update slot in fragment_game.xml
     */
    private fun updateSlots(result: TurnResult, currentSlots: List<Slot>, lastRoll: Int) {
        if (result.clearSlots) currentSlots.clear()

        currentSlots.firstOrNull { it.lastRolled }?.apply { lastRolled = false }

        currentSlots.getOrNull(lastRoll - 1)?.also { slot ->
            if (!result.clearSlots && slot.canBeFilled) slot.isFilled =  true

            slot.lastRolled = true
        }

        slots.notifyChange()
    }

    /**
     * @since generateCurrentStandings() - converts information about Players to String
     * and update the game state in the fragment_game.xml
     */
    private fun generateCurrentStandings(players: List<Player>,
                                         headerText: String = "Current Standings:") =

        //joinToString() - Creates a string from all the elements separated using separator and using the given prefix and postfix if supplied
        players.sortedBy { it.pennies }.joinToString(separator = "\n", prefix = "$headerText\n") {
            "\t${it.playerName} - ${it.pennies} pennies"
        }

    /**
     * @since generateTurnText() - converts information about Players to String
     * and update the game history in the fragment_game.xml
     */
    private fun generateTurnText(result: TurnResult): String {
        if (clearText) currentTurnText.value = ""
        clearText = result.turnEnd != null

        val currentText = currentTurnText.value ?: ""
        val currentPlayerName = result.currentPlayer?.playerName ?: "???"

        return when {

            //TurnRest - based logic and text
            result.isGameOver -> //Game's over, let's get a summary
                """|Game over!
                    |$currentPlayerName is winner!
                    |
                    |${generateCurrentStandings(this.players, "Final Scores:\n")}
                    |}}
                """.trimMargin()


            result.turnEnd == TurnEnd.Bust -> "$currentPlayerName busted, got some pennies"
            result.turnEnd == TurnEnd.Pass -> "$currentPlayerName passed"

            result.lastRoll != null -> //Roll test
                //"""___""" - текст без обработки
                // | - указатель начала строки
                "$currentText\n$currentPlayerName rolled a ${result.lastRoll}."

            else -> ""
        }
    }

    /*
    private fun generateTurnText(result: TurnResult): String {
//        if (clearText) currentTurnText.value = ""
//        clearText = result.turnEnd != null

//        val currentText = currentTurnText.value ?: ""
        val currentPlayerName = result.currentPlayer?.playerName ?: "???"

        val currentText =  when {

            //TurnRest - based logic and text
            result.isGameOver -> //Game's over, let's get a summary
                """|Game over!
                    |$currentPlayerName is winner!
                    |
                    |${generateCurrentStandings(this.players, "Final Scores:\n")}
                    |}}
                """.trimMargin()


            result.turnEnd == TurnEnd.Bust -> "\n${result.previousPlayer?.playerName} busted, got some pennies\n"
            result.turnEnd == TurnEnd.Pass -> "\n${result.previousPlayer?.playerName} passed\n"

            result.lastRoll != null -> //Roll test
                //"""___""" - текст без обработки
                // | - указатель начала строки
                "\n$currentPlayerName rolled a ${result.lastRoll}."

            else -> ""
        }
        return currentText
    }
     */

    /**
     * @since playAITurn() - AI player make a turn (GameViewModel)
     */
    private fun playAITurn() {
        viewModelScope.launch {
            delay(1000)
            slots.value?.let { currentSlot ->
                val currentPlayer = players.firstOrNull { it.isRolling }

                if (currentPlayer != null && !currentPlayer.isHuman) {
                    GameHandler.playAITurn(
                        players,
                        currentPlayer,
                        currentSlot,
                        canPass.value == true)?.let { result ->
                        updateFromGameHandler(result)
                    }
                }
            }
        }
    }
}

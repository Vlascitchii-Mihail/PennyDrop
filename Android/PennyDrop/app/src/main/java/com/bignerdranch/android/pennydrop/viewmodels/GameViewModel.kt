package com.bignerdranch.android.pennydrop.viewmodels

import android.app.Application
import androidx.lifecycle.*
import androidx.preference.PreferenceManager
import com.bignerdranch.android.pennydrop.data.*
import com.bignerdranch.android.pennydrop.game.GameHandler
import com.bignerdranch.android.pennydrop.game.TurnEnd
import com.bignerdranch.android.pennydrop.game.TurnResult
import com.bignerdranch.android.pennydrop.types.Player
import com.bignerdranch.android.pennydrop.types.Slot
import com.bignerdranch.android.pennydrop.types.clear
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.OffsetDateTime

/**
 * @property currentPlayer current player in the game
 * @property canRoll opportunity to the current player to make a move
 * @property canPass opportunity to the current player to make a pass
 * @property currentTurnText game's history
 * @property currentStandingsText result of the game
 */
//AndroidViewModel(application) - parent class which allows us to refer to the Application's object
//to get the context in ViewModel, ViewModel с учетом контекста приложения.
class GameViewModel(application: Application): AndroidViewModel(application) {

    private var clearText = false

    private val repository: PennyDropRepository

    //MediatorLiveData<>() - LiveData subclass which can observe other
    // LiveData objects and react on OnChanged events from them.
    val currentGame = MediatorLiveData<GameWithPlayers>()

    val currentGameStatuses: LiveData<List<GameStatus>>

    //current player in the game
//    val currentPlayer = MutableLiveData<Player?>()
    //Transformations - tools for change LifeData when it changes
    //map() - Возвращает LiveData, сопоставленный с входным источником LiveData,
    // применяя mapFunction к каждому значению, установленному в источнике.
    val currentPlayer = Transformations.map(this.currentGame) { gameWithPlayers ->
        gameWithPlayers?.players?.firstOrNull { it.isRolling }
    }

    //result of the game
    var currentStandingsText = Transformations.map(this.currentGame) { gameWithPlayers ->
        gameWithPlayers?.players?.let { players ->
            this.generateCurrentStandings(players)
        }
    }

    private var players: List<Player> = emptyList()

    val slots = Transformations.map(this.currentGame) { gameWithPlayers ->
        Slot.mapFromGame(gameWithPlayers?.game)
    }

    /**
     * listen to the currentPlayer using Transformations.map()
     */
    val canRoll = Transformations.map(this.currentPlayer) { player ->
        player?.isHuman == true && currentGame.value?.game?.canRoll == true
    }

    /**
     * listen to the currentPlayer using Transformations.map()
     */
    val canPass = Transformations.map(this.currentPlayer) { player ->
        player?.isHuman == true && currentGame.value?.game?.canPass == true
    }

    //game's history
    val currentTurnText = MutableLiveData("")

    //getDefaultSharedPreferences() - Gets a SharedPreferences instance that points to
    // the default file that is used by the preference framework in the given context.
    private val prefs = PreferenceManager.getDefaultSharedPreferences(application)

    init {

        //create database's variable to get repository's variable
        //viewModelScope - CoroutineScope tied to this ViewModel. This scope will be canceled
        // when ViewModel will be cleared, i.e ViewModel.onCleared is called
        this.repository = PennyDropDatabase.getDatabase(application, viewModelScope).pennyDropDao()
            .let { dao -> PennyDropRepository.getInstance(dao) }

        this.currentGameStatuses = this.repository.getCurrentGameStatuses()

        //addSource() - Starts to listen the given source LiveData, onChanged observer
        // will be called when source value was changed.
        //Params: source – the LiveData to listen to
        //onChanged – The observer that will receive the event
        this.currentGame.addSource(this.repository.getCurrentGameWithPlayers()) { gameWithPlayers ->
            updateCurrentGame(gameWithPlayers, this.currentGameStatuses.value)
        }

        //addSource() - Starts to listen the given source LiveData, onChanged observer
        // will be called when source value was changed.
        //Params: source – the LiveData to listen to
        //onChanged – The observer that will receive the event
        this.currentGame.addSource(this.currentGameStatuses) { gameStatuses ->
            updateCurrentGame(this.currentGame.value, gameStatuses)
        }
    }

    private fun updateCurrentGame(gameWithPlayers: GameWithPlayers?, gameStatuses: List<GameStatus>?) {
        this.currentGame.value = gameWithPlayers?.updateStatuses(gameStatuses)
    }

//    fun startGame(playersForNewGame: List<Player>) {
//
//        //getting the players fom PickPlayersViewModel
//        this.players = playersForNewGame
//        this.currentPlayer.value = this.players.firstOrNull().apply {
//            this?.isRolling = true
//        }
//
//        this.canRoll.value = true
//        this.canPass.value = false
//
//        slots.value?.clear()
//        slots.notifyChange()
//
//        currentTurnText.value = "The game has begun!\n"
//        currentStandingsText.value = generateCurrentStandings(this.players)
//    }

    /**
     * send players in database for starting the game
     */
    suspend fun startGame(playersForNewGame: List<Player>) {
        repository.startGame(playersForNewGame, prefs?.getInt("pennyCount", Player.defaultPennyCount))
    }

    /**
     * fun is colling from fragment_game.xml, this.roll() calls another fun roll() from GameHandler
     * and return result in fragment_game.xml
     */
    fun roll() {
        val game = this.currentGame.value?.game
        val players = this.currentGame.value?.players
        val currentPlayer = this.currentPlayer.value
        val slots = this.slots.value

            //Comparing with true saves us from a null check
        if (game != null && players !== null && currentPlayer != null && slots != null && game.canRoll) {
                updateFromGameHandler(GameHandler.roll(players, currentPlayer, slots))
        }
    }

    /**
     * fun is colling from fragment_game.xml, this.roll() calls another fun pass() from GameHandler
     * and return result in fragment_game.xml
     */
    fun pass() {
        val game = this.currentGame.value?.game
        val players = this.currentGame.value?.players
        val currentPlayer = this.currentPlayer.value

        if (game != null && players != null &&  currentPlayer != null && game.canPass) {
            updateFromGameHandler(GameHandler.pass(players, currentPlayer))
        }
    }

//    /**
//     * @since notifyChange update LiveData with the same value for sending this data to LiveData listeners
//     */
//    private fun <T> MutableLiveData<List<T>>.notifyChange() {
//        this.value = this.value
//    }

    /**
     * update the UI of the fragment_game.xml
     */
    private  fun updateFromGameHandler(result: TurnResult) {
        val game = currentGame.value?.let { currentGameWithPlayers ->
            currentGameWithPlayers.game.copy(
                gameState = if (result.isGameOver) GameState.Finished else GameState.Started,
                lastRoll = result.lastRoll,
                filledSlots = updateFilledSlots(result, currentGameWithPlayers.game.filledSlots),
                currentTurnText = generateTurnText(result),
                canRoll = result.canRoll,
                canPass = result.canPass,
                endTime = if (result.isGameOver) OffsetDateTime.now() else null
            )
        } ?: return

        val statuses = currentGameStatuses.value?.map { status ->
            when(status.playerId) {
                result.previousPlayer?.playerId -> { status.copy(
                    isRolling = false,
                    pennies = status.pennies + (result.coinChangeCount ?: 0)
                )}

                result.currentPlayer?.playerId -> { status.copy(
                    isRolling = !result.isGameOver,
                    pennies = status.pennies + if (!result.playerChanged) {
                        result.coinChangeCount ?: 0
                    } else 0
                )}

                else -> status
            }
        } ?: emptyList()

        //CoroutineScope tied to this ViewModel.
        viewModelScope.launch {
            repository.updateGameAndStatuses(game, statuses)
            if (result.currentPlayer?.isHuman == false) playAITurn()
        }
    }

    private fun updateFilledSlots(result: TurnResult, filledSlots: List<Int>) =
        when {
            result.clearSlots -> emptyList()
            result.lastRoll != null && result.lastRoll != 6 -> filledSlots + result.lastRoll
            else -> filledSlots
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
    }

    /**
     * converts information about Players to String and update the game state in the fragment_game.xml
     */
    private fun generateCurrentStandings(players: List<Player>,
                                         headerText: String = "Current Standings:") =

        //joinToString() - Creates a string from all the elements separated using separator and using the given prefix and postfix if supplied
        players.sortedBy { it.pennies }.joinToString(separator = "\n", prefix = "$headerText\n") {
            "\t${it.playerName} - ${it.pennies} pennies"
        }

    /**
     * converts information about Players to String
     * and update the game history in the fragment_game.xml
     */
    private fun generateTurnText(result: TurnResult): String {

        val currentText = currentGame.value?.game?.currentTurnText ?: ""
//        clearText = result.turnEnd != null

        val currentPlayerName = result.currentPlayer?.playerName ?: "???"

        return when {

            //TurnRest - based logic and text
            result.isGameOver -> generateGameOverText()

            result.turnEnd == TurnEnd.Bust -> "$currentText\n${result.previousPlayer?.playerName} busted, got some pennies\n"
            result.turnEnd == TurnEnd.Pass -> "$currentText\n${result.previousPlayer?.playerName} passed\n"

            result.lastRoll != null ->
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

    private fun generateGameOverText() : String {
        val statuses = this.currentGameStatuses.value
        val players = this.currentGame.value?.players?.map { player ->
            player.apply {
                this.pennies = statuses?.firstOrNull { it.playerId == playerId }
                    ?.pennies ?: Player.defaultPennyCount
            }
        }

        val winningPLayer = players?.firstOrNull { !it.penniesLeft() || it.isRolling }
            ?.apply { this.pennies = 0 }

        if (players == null || winningPLayer == null) return "N/A"

        //Roll test
        //"""___""" - текст без обработки
        // | - указатель начала строки
        return """|Game over!
                    |${winningPLayer.playerName} is winner!
                    |
                    |${generateCurrentStandings(players, "Final Scores:\n")}
                """.trimMargin()
    }


    /**
     * AI player make a turn (GameViewModel)
     */
    private suspend fun playAITurn() {

        /**
         * getBoolean() - check the state of the SwitchPreferenceCompat
         * @param "fastAI" - SwitchPreferenceCompat key
         * @param false - default value if "fastAI" is null
         */
        delay(if (prefs.getBoolean("fastAI", false))100 else 1000)
        val game = currentGame.value?.game
        val players = currentGame.value?.players
        val currentPlayer = currentPlayer.value
        val slots = slots.value

        if (game != null && players != null && currentPlayer != null && slots != null) {
            GameHandler.playAITurn(players, currentPlayer, slots, game.canPass)?.let { result->
                updateFromGameHandler(result)
            }
        }
    }
}

package com.bignerdranch.android.pennydrop.game

import com.bignerdranch.android.pennydrop.types.Player
import com.bignerdranch.android.pennydrop.types.Slot
import kotlin.random.Random

//handle the game logic
object GameHandler {

    /**
     * @since roll() - return the result of rolling the die
     */
    fun roll(players: List<Player>, currentPlayer: Player, slots: List<Slot>)  =
        rollDie().let { lastRoll ->

            //choose a slot
            slots.getOrNull(lastRoll - 1)?.let { slot ->

                if (slot.isFilled) {

                    //Player busts, play continues next player
                    //Игрок забирает все монкеты на слотах, игра продолжается следующим игроком
                    TurnResult(
                        lastRoll,

                        //count() - Returns the number of elements matching the given predicate.
                        coinChangeCount = slots.count { it.isFilled },
                        clearSlots = true,
                        turnEnd = TurnEnd.Bust,
                        previousPlayer = currentPlayer,
                        currentPlayer = nextPlayer(players, currentPlayer),
                        playerChanged = true,
                        canRoll = true,
                        canPass = false
                    )

                } else {

                    //check quantity of the pennies
                    if (!currentPlayer.penniesLeft(true)) {
                        //Player wins
                        TurnResult(
                            lastRoll,
                            currentPlayer = currentPlayer,
                            coinChangeCount = -1,
                            isGameOver = true,
                            turnEnd = TurnEnd.Win,
                            canRoll = false,
                            canPass = false
                        )

                    } else {

                        //Game continues
                        TurnResult(
                            lastRoll,
                            currentPlayer = currentPlayer,
                            canRoll = true,
                            canPass = true,
                            coinChangeCount = -1
                        )
                    }
                }

                //turn result is created there
            } ?: TurnResult(isGameOver = true)
        }

    /**
     * @since pass() - skip a turn if player made at least one
     */
    fun pass(players: List<Player>, currentPlayer: Player): TurnResult = TurnResult(
            previousPlayer = currentPlayer,
            currentPlayer = nextPlayer(players, currentPlayer),
            playerChanged = true,
            turnEnd = TurnEnd.Pass,
            canRoll = true,
            canPass = false
        )

    //nextInt() - Получает следующий случайный Int от генератора случайных
    // чисел в указанном диапазоне. Генерирует случайное значение Int, равномерно
    // распределенное между указанными границами от (включительно) и до (исключительно)
    private fun rollDie(sides: Int = 6) = Random.nextInt(1, sides + 1)

    /**
     * @param nextPlayer - grab the next player
     */
    private fun nextPlayer(players: List<Player>, currentPlayer: Player): Player? {
        val currentIndex = players.indexOf(currentPlayer)
        val nextIndex = (currentIndex + 1) % players.size
        return players[nextIndex]
    }

    /**
     * @since playAITurn() - AI player make a turn (GameHolder)
     */
    fun playAITurn(
        players: List<Player>,
        currentPlayer: Player,
        slots: List<Slot>,
        canPass: Boolean = false
    ): TurnResult? =
        currentPlayer.selectedAI?.let { ai ->
            if (!canPass || ai.rollAgain(slots)) {
                roll(players, currentPlayer, slots)
            } else pass(players, currentPlayer)
        }
}
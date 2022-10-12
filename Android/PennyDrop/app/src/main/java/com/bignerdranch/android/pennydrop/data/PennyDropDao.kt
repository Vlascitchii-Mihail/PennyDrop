package com.bignerdranch.android.pennydrop.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.bignerdranch.android.pennydrop.types.Player
import com.bignerdranch.android.pennydrop.data.Game
import java.time.OffsetDateTime

//@Dao - абстрактный кдасс доступа к базе данных
@Dao
abstract class PennyDropDao {
    /**
     * @param playerName send the parameter to the Query
     */
    @Query("SELECT * FROM players WHERE playerName = :playerName")
    abstract fun getPlayer(playerName: String): Player?

    @Insert
    abstract suspend fun insertGame(game: Game): Long

    @Insert
    abstract suspend fun insertPlayer(player: Player): Long

    @Insert
    abstract suspend fun insertPlayers(players: List<Player>): List<Long>

    @Update
    abstract suspend fun updateGame(game: Game)

    //@Transaction - annotation tells  Room that the function you’re calling
    // references multiple tables and the data should be retrieved in a
    // single atomic  operation. We’re getting data from both the Games
    //and the Players tables
    @Transaction

    //While the query only mentions the games table, we’re pulling in
    // data from both tables because of the @Relation annotation and the
    // @Junction on the GameWithPlayers class.
    /**
     * @param DESC LIMIT - quantity of results
     */
    @Query("SELECT * FROM games ORDER BY startTime DESC LIMIT 1")
    abstract fun getCurrentGameWithPlayers(): LiveData<GameWithPlayers>

    @Transaction
    @Query("""SELECT * FROM game_statuses WHERE gameId =
        (SELECT gameId FROM games WHERE endTime IS NULL 
ORDER BY startTime DESC LIMIT 1) ORDER BY gamePlayerNumber
    """)
    abstract fun getCurrentGameStatuses(): LiveData<List<GameStatus>>

    //Room automatically add @Transaction annotation to UPDATE QUERY
    //:endDate :gameState - reference to variable in SQLite query
    //it works because of the Converter's functions
    @Query("""UPDATE games SET endTime = :endDate, gameState = :gameState 
        WHERE endTime IS NULL
    """)
    abstract suspend fun closeOpenGames(endDate: OffsetDateTime = OffsetDateTime.now(),
    gameState: GameState = GameState.Cancelled)

    //we can write List<GameStatus> in database because of GameStatus is entity in database
    @Insert
    abstract suspend fun insertGameStatuses(gameStatuses: List<GameStatus>)

    /**
     * start the game
     * @param pennyCount - default penny value from setting
     */
    @Transaction
    open suspend fun startGame(players: List<Player>, pennyCount: Int? = null): Long {
        this.closeOpenGames()

        val gameId = this.insertGame(
            Game(
                gameState = GameState.Started,
                currentTurnText = "The game has begun!\n",
                canRoll = true
            )
        )

        val playersId = players.map { player ->
            getPlayer(player.playerName)?.playerId ?: insertPlayer(player)
        }

        this.insertGameStatuses(

            //mapIndexed() - Returns a list containing the results of applying
            // the given transform function to each element and its index in the
            // original collection.
            playersId.mapIndexed { index, playerId ->
                GameStatus(
                    gameId,
                    playerId,
                    index,
                    index == 0,
                    pennyCount ?: Player.defaultPennyCount
                )
            }
        )

        return gameId
    }

    @Update
    abstract suspend fun updateGameStatuses(gameStatuses: List<GameStatus>)

    @Transaction
    open suspend fun updateGameAndStatuses(game: Game, statuses: List<GameStatus>) {
        this.updateGame(game)
        this.updateGameStatuses(statuses)
    }

    @Transaction
    @Query("""
        SELECT * FROM game_statuses WHERE gameId IN (
        SELECT gameId FROM games WHERE gameState = :finishedGameState
        )
    """)
    abstract fun getCompletedGameStatusesWithPlayers(
        finishedGameState: GameState = GameState.Finished
    ): LiveData<List<GameStatusWithPlayer>>
}
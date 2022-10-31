package com.bignerdranch.android.pennydrop.data

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.bignerdranch.android.pennydrop.types.Player

//reuse GameStatus for playing the different games with the same players
data class GameWithPlayers(

    //Room будет рассматривать поля класса Game при сопоставлении строки SQLite как поля класса GameWithPlayers,
    //доступ к полям Game щсуществляется через поле game: Game в классе GameWithPlayers
    //(включенный)Помечает поле Entity или POJO, чтобы разрешить прямые ссылки на вложенные
    // поля (т. е. поля класса аннотированного поля) в SQL-запросах.
    //Родительская сущность
    @Embedded val game: Game,

    //Возможность при SQL запросе к одной таблице, извлечь данные из 2 таблиц
    //Удобная аннотация, которую можно использовать в POJO для автоматического
    // извлечения объектов отношения. Когда POJO возвращается из запроса,
    // все его отношения также извлекаются Room.
    @Relation(

        //primaryKey of the parent class Game
        parentColumn = "gameId",

        //primaryKey of the Player
        entityColumn = "playerId",

        /**
         * @param associateBy - Сущность или представление, которое будет использоваться
         * в качестве ассоциативной таблицы (также известной как соединительная таблица)
         * при выборке связанных сущностей.
         * @param Junction() - (соединение) Объекты соединения вносит ссылку на класс GameStatus,
         * который соединяет 2 таблицы вместе
         */
        associateBy = Junction(GameStatus::class)
    )
    val players: List<Player>
) {
    fun updateStatuses(gameStatuses: List<GameStatus>?) =
        if (gameStatuses != null) {

            //copy() a new GameWithPlayers exemplar
            this.copy(
                players = players.map { player ->
                    gameStatuses.firstOrNull { it.playerId == player.playerId }
                        ?.let { gameStatus ->
                            player.apply {
                                pennies = gameStatus.pennies
                                isRolling = gameStatus.isRolling
                                gamePlayingNumber = gameStatus.gamePlayerNumber
                            }
                        } ?: player
                }.sortedBy { it.gamePlayingNumber }
            )
        } else this
}
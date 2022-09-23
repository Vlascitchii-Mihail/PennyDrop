package com.bignerdranch.android.pennydrop.data

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.bignerdranch.android.pennydrop.types.Player

//reuse GameStatus for playing the different games with the same players
data class GameWithPlayers(

    //(включенный)Помечает поле Entity или POJO, чтобы разрешить прямые ссылки на вложенные
    // поля (т. е. поля класса аннотированного поля) в SQL-запросах.
    //Родительская сущность
    @Embedded val game: Game,

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
}
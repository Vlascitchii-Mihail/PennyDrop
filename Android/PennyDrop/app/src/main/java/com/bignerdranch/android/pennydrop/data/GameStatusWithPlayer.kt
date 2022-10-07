package com.bignerdranch.android.pennydrop.data

import androidx.room.Embedded
import androidx.room.Relation
import com.bignerdranch.android.pennydrop.types.Player

data class GameStatusWithPlayer(

    //Room будет рассматривать поля класса Game при сопоставлении строки SQLite как поля класса GameStatus,
    //доступ к полям Game щсуществляется через поле game: Game в классе GameStatus
    //(включенный)Помечает поле Entity или POJO, чтобы разрешить прямые ссылки на вложенные
    // поля (т. е. поля класса аннотированного поля) в SQL-запросах.
    //Родительская сущность
    @Embedded val gameStatus: GameStatus,

    //Возможность при SQL запросе к одной таблице, извлечь данные из 2 таблиц
    //Удобная аннотация, которую можно использовать в POJO для автоматического
    // извлечения объектов отношения. Когда POJO возвращается из запроса,
    // все его отношения также извлекаются Room.
    @Relation(

        //primaryKey of the parent class Game
        parentColumn = "playerId",

        //primaryKey of the Player
        entityColumn = "playerId"
    )
    val player: Player
) {

}
package com.bignerdranch.android.pennydrop.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.bignerdranch.android.pennydrop.types.Player

@Entity(
    tableName = "game_statuses",

    //The list of Primary Key column names.
    //If you would like to define an auto generated primary key, you can use
    // PrimaryKey annotation on the field with PrimaryKey.autoGenerate() set to true.
    primaryKeys = ["gameId", "playerId"],

    //List of ForeignKey constraints on this entity.
    //Returns: The list of ForeignKey constraints on this entity.
    foreignKeys = [

        /**
         * @param entity - the parent Entity to reference.
         * @param parentColumns - The list of column names in the PARENT Entity.
         * Number of columns must match the number of columns specified in childColumns().
         * Количество столбцов должно соответствовать количеству столбцов, указанному в childColumns().
         * @param childColumns - The list of column names in the CURRENT Entity(GameStatus).
         * Number of columns must match the number of columns specified in parentColumns().
         */
        ForeignKey(

            //The parent Entity to reference.
            entity = Game::class,
            parentColumns = ["gameId"],
            childColumns = ["gameId"]
        ),
        ForeignKey(

            //The parent Entity to reference.
            entity = Player::class,
            parentColumns = ["playerId"],
            childColumns = ["playerId"]
        )
    ]
)

/**
 * merge the tables Player and Game
 * responsible for holding the user's current(or final) state in the game
 */
data class GameStatus(
    val gameId: Long,

    //Помогает ускорить поиск при использовании GameStatus в качестве соединения./
    // Разрешает конкретную настройку столбца, связанного с этим полем.
    //Например, вы можете указать имя столбца для поля или изменить сходство типа столбца.
    @ColumnInfo(index = true)val playerId: Long,
    val gamePlayerNumber: Int,
    val isRolling: Boolean = false,
    val pennies: Int = Player.defaultPennyCount
) {
}
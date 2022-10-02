package com.bignerdranch.android.pennydrop.data

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bignerdranch.android.pennydrop.game.AI
import com.bignerdranch.android.pennydrop.types.Player
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * @param entities SomeClass::class - table of the database or the list of entities included in the database
 * @param version database version
 */
//Marks a class as a RoomDatabase.
@Database(entities = [Game::class, Player::class, GameStatus::class], version = 1, exportSchema = false)

//announce that database have a converter
@TypeConverters(Converters::class)
abstract class PennyDropDatabase: RoomDatabase() {

    /**
     * @return an exemplar with type of
     * the PennyDropDao class
     */
    abstract fun pennyDropDao(): PennyDropDao

    companion object {

//     @Volatile - Помечает вспомогательное поле JVM аннотированного
//     свойства как изменчивое, что означает, что записи в это поле немедленно
//     становятся видимыми для других потоков.
        @Volatile
        private var instance: PennyDropDatabase? = null

        /**
         * @return an database's exemplar
         */
        fun getDatabase(context: Context, scope: CoroutineScope): PennyDropDatabase =

            //synchronized() - Выполняет данный функциональный блок, удерживая монитор блокировки данного объекта.
            this.instance ?: synchronized(this) {

                //Room.databaseBuilder() - Creates a RoomDatabase.Builder for a persistent database.
                /**
                 * @param context app's context - PennyDrop.app.main
                 * @param PennyDropDatabase::class.java database's class
                 * @param PennyDropDatabase database's name
                 */
                val instance = Room.databaseBuilder(
                    context,
                    PennyDropDatabase::class.java,
                "PennyDropDatabase").addCallback(object: RoomDatabase.Callback() {

                    //add AI players at creating database
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        scope.launch {
                            instance?.pennyDropDao()?.insertPlayers(
                                AI.basicAI.map(AI::toPlayer)
                            )
                        }
                    }
                }).build()

                this.instance = instance

                instance
            }
    }
}
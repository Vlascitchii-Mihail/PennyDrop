package com.bignerdranch.android.pennydrop.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope

/**
 * @param entities[] - [Crime::class] - table of database or The list of entities included in the database
 * @param version 3 - database version
 */
//Marks a class as a RoomDatabase.
@Database(entities = [], version = 1, exportSchema = false)

abstract class PennyDropDatabase: RoomDatabase() {
    abstract fun pennyDropDao(): PennyDropDao

    /**
     * @since @Volatile - Помечает вспомогательное поле JVM аннотированного
     * свойства как изменчивое, что означает, что записи в это поле немедленно
     * становятся видимыми для других потоков.
     */
    companion object {
        @Volatile private var instance: PennyDropDatabase? = null

        /**
         * @since getDatabase() - return database's exemplar
         */
        fun getDatabase(context: Context, scope: CoroutineScope): PennyDropDatabase =

            //synchronized() - Выполняет данный функциональный блок, удерживая монитор блокировки данного объекта.
            this.instance ?: synchronized(this) {

                //creating a database
                /**
                 * @param context - app's context - PennyDrop.app.main
                 * @param PennyDropDatabase::class.java - database's class
                 * @param PennyDropDatabase - database's name
                 */
                val instance = Room.databaseBuilder(
                    context,
                    PennyDropDatabase::class.java,
                "PennyDropDatabase").build()

                this.instance = instance

                instance
            }
    }
}
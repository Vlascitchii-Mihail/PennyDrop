package data_base

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bignerdranch.android.criminalintent.Crime
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
//@Database - database in app
/**
 * @param - [Crime::class] - table of database
 * @param 3 - database version
 */
@Database(entities = [Crime::class], version = 3)

//Converter of database
@TypeConverters(CrimeTypeConverters::class)
abstract class CrimeDatabase: RoomDatabase() {

    //Помогает вызывать функции из CrimeDao
    //Подключает Dao к database
    abstract fun crimeDao(): CrimeDao
}

//creating field, uses for executing the migration
//Migration(1, 2) - constructor
/**
 * @param 1 - old database version
 * @param 2 - new database version
 */
val migration_1_2 = object : Migration(1, 2) {

    //executing the migration
    override fun migrate(database: SupportSQLiteDatabase) {

        //Выполните один оператор SQL, который не возвращает никаких данных.
        //ALTER TABLE - adding new column in database's table
        database.execSQL("ALTER TABLE Crime ADD COLUMN suspect TEXT NOT NULL DEFAULT''")
    }
}

val migration_2_3 = object: Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE Crime ADD COLUMN suspectPhoneNumber TEXT NOT NULL DEFAULT ''")
    }
}
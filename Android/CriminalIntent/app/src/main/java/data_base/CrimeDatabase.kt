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
 */
@Database(entities = [Crime::class], version = 3)

//Converter of database
@TypeConverters(CrimeTypeConverters::class)
abstract class CrimeDatabase: RoomDatabase() {

    //Помогает вызывать функции из CrimeDao
    //Подключает Dao к database
    abstract fun crimeDao(): CrimeDao
}

val migration_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE Crime ADD COLUMN suspect TEXT NOT NULL DEFAULT''")
    }
}

val migration_2_3 = object: Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE Crime ADD COLUMN suspectPhoneNumber TEXT NOT NULL DEFAULT ''")
    }
}
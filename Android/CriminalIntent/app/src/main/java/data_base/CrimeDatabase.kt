package data_base

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bignerdranch.android.criminalintent.Crime
import androidx.room.TypeConverters

@Database(entities = [Crime::class], version = 1)
@TypeConverters(CrimeTypeConverters::class)
abstract class CrimeDatabase: RoomDatabase() {
    abstract fun crimeDao(): CrimeDao
}
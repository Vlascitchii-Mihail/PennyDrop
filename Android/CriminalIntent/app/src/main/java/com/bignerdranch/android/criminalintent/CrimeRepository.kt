package com.bignerdranch.android.criminalintent

import android.content.Context
import data_base.CrimeDatabase
import androidx.room.Room
import java.util.UUID
import androidx.lifecycle.LiveData
import data_base.migration_1_2
import data_base.migration_2_3
import java.util.concurrent.Executors
import java.io.File

private const val DATABASE_NAME = "crime-database"

//Singleton
//private constructor forbids (запрещает) creating a new object
class CrimeRepository private constructor(context: Context) {

    //creating a reference to the database CrimeDatabase
    //CrimeRepository() Создает конкретную реализацию базы данных
    /**
     * @param context.applicationContext - app's context - CriminalIntentApplication : Application
     * @param CrimeDatabase::class.java - database's class
     * @param DATABASE_NAME - database's name
     */
    private val database: CrimeDatabase = Room.databaseBuilder(
        context.applicationContext, CrimeDatabase::class.java, DATABASE_NAME
    ).addMigrations(migration_2_3).build()

    private val executor = Executors.newSingleThreadExecutor()

    private val filesDir = context.applicationContext.filesDir

    //provides access to CrimeDao interface
    private val crimeDao = database.crimeDao()

    //provides access to CrimeDao.getCrimes()
    //LiveData<List<Crime>> provide an access to the data between 2 threads and starts the functions in the second thread
    fun getCrimes(): LiveData<List<Crime>> = crimeDao.getCrimes()

    ////provides access to CrimeDao.getCrime()
    //LiveData<List<Crime>> provide an access to the data between 2 threads and starts the functions in the second thread
    fun getCrime(id: UUID) : LiveData<Crime?> = crimeDao.getCrime(id)

    fun updateCrime(crime: Crime) {
        executor.execute {
            crimeDao.updateCrime(crime)
        }
    }

    fun addCrime(crime: Crime) {
        executor.execute{
            crimeDao.addCrime(crime)
        }
    }

    fun getPhotoFile(crime: Crime): File = File(filesDir, crime.photoFileName)

    companion object {

        //CrimeRepository variable
        private var INSTANCE: CrimeRepository? = null

        //creating and initialising new CrimeRepository object
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = CrimeRepository(context)
            }
        }

        //CrimeRepository provider
        fun get(): CrimeRepository {
            return INSTANCE ?: throw IllegalStateException("Crime Repository must be initialized")
        }
    }
}
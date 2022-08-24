package com.bignerdranch.android.criminalintent

import android.app.Application

//provides lifecycle of app
//Создается когда приложение запускается и уничтожается когда завершается процесс приложения
class CriminalIntentApplication: Application() {

    //starts with application creates and initializes CrimeRepository's object
    override fun onCreate() {
        super.onCreate()

        //creating and initialising new CrimeRepository object
        //using the context of the app
        CrimeRepository.initialize(this)
    }
}
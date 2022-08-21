package com.bignerdranch.android.criminalintent

import androidx.lifecycle.ViewModel

class CrimeListViewModel: ViewModel() {
    var dataListSize: Int? = 0
//    val crimes = mutableListOf<Crime>()

//    init {
//        for (i in 0 until 100) {
//            val crime = Crime()
//            crime.title = "Crime #$"
//            crime.isSolved = i % 2 == 0
//            if (i % 5 == 0) crime.requiresPolice = true
//            crimes += crime
//        }
//    }

    //provides access CrimeRepository object
    private val crimeRepository = CrimeRepository.get()

    //returns a list of crimes
    val crimeListLiveData = crimeRepository.getCrimes()

    //adding a new crime from the menu
    fun addCrime(crime: Crime) {
        crimeRepository.addCrime(crime)
    }
}
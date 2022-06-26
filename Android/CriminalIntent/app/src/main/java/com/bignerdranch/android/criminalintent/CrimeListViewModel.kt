package com.bignerdranch.android.criminalintent

import androidx.lifecycle.ViewModel

class CrimeListViewModel: ViewModel() {
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

    private val crimeRepository = CrimeRepository.get()
    val crimeListLiveData = crimeRepository.getCrimes()
}
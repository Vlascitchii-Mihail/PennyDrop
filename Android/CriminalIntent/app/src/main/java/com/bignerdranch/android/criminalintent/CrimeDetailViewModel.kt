package com.bignerdranch.android.criminalintent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import java.util.UUID
import java.io.File

//ViewModel for CrimeFragment
class CrimeDetailViewModel(): ViewModel() {
    private val crimeRepository = CrimeRepository.get()

    //current crime's ID
    private val crimeIdLiveData = MutableLiveData<UUID>()

    //Загрузка данных из базы данных при изменении объекта crimeIdLiveData
    //LiveData listener
    //Методы преобразования LiveData.
    //Transformations() - Преобразование данных в реальном времени, <<триггер - ответ>> между 2я объектами LiveData.
    /**
     * @param crimeIdLiveData - trigger
     */
    // Эти методы обеспечивают функциональную композицию и делегирование экземпляров LiveData.
    //switchMap() - Возвращает LiveData, сопоставленный с входным источником LiveData,
    // применяя switchMapFunction к каждому значению, установленному в источнике.
    var crimeLiveData: LiveData<Crime?> = Transformations.switchMap(crimeIdLiveData) {

        //upload data from database
        // crimeId == crimeIdLiveData
            crimeId -> crimeRepository.getCrime(crimeId)
    }

    //uploading crime using ID
    fun loadCrime(crimeId: UUID) {

        //Задает значение. Если есть активные наблюдатели, значение будет отправлено им.
        crimeIdLiveData.value = crimeId
    }

    //saving user's input
    fun saveCrime(crime: Crime) {
        crimeRepository.updateCrime(crime)
    }

    //receiving a data about the file location
    fun getPhotoFile(crime: Crime): File = crimeRepository.getPhotoFile(crime)
}
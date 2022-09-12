package com.bignerdranch.android.photogallery

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.bignerdranch.android.photogallery.api.PhotoResponse
import retrofit2.Call
import kotlinx.coroutines.flow.Flow
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import android.app.Application
import androidx.lifecycle.AndroidViewModel

//AndroidViewModel - provides access to the app's context
class PhotoGalleryViewModel(private val app: Application): AndroidViewModel(app) {

    //keeps all the photos - List with photos
    val galleryItemLiveData: LiveData<List<GalleryItem>>
//    val usersFlow: Flow<PagingData<GalleryItem>>

    private val flickrFetchr = FlickrFetchr()
    private val mutableSearchTerm = MutableLiveData<String>()

    val searchTerm: String get() = mutableSearchTerm.value ?: ""

    init {

        //uploading data from the SharedPreferences
        mutableSearchTerm.value = QueryPreferences.getStoredQuery(app)
//        galleryItemLiveData = FlickrFetchr().searchPhotos("car")

        //LiveData listener
        //Методы преобразования LiveData.
        //Transformations() - Преобразование данных в реальном времени, <<триггер - ответ>> между 2я объектами LiveData.
        /**
         * @param mutableSearchTerm - trigger
         */
        // Эти методы обеспечивают функциональную композицию и делегирование экземпляров LiveData.
        //switchMap() - Возвращает LiveData, сопоставленный с входным источником LiveData,
        // применяя switchMap к каждому значению, установленному в источнике
        galleryItemLiveData = Transformations.switchMap(mutableSearchTerm) {
            searchTerm -> if (searchTerm.isBlank()) {

                //get photos when the app starts
                flickrFetchr.fetchPhotos()

            //searching photos
              } else flickrFetchr.searchPhotos(searchTerm)
        }
        Log.d("PhotoGalleryViewModel", "New ViewModel")
//        usersFlow = a
    }

    fun fetchPhotos(query: String = "") {

        //writing data in the SharedPreferences
        QueryPreferences.setStoredQuery(app, query)
        mutableSearchTerm.value = query
    }

    companion object {
        lateinit var flow: Flow<PagingData<GalleryItem>>

        lateinit var webCall: Call<PhotoResponse>
        fun fll(call:  Call<PhotoResponse>) {
            webCall = call
        }

        fun cancelCall() {
            if (webCall.isExecuted)
                webCall.cancel()
//            Log.d("PhotoGalleryViewModel", "Call canceled")
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("PhotoGalleryViewModel", "Canceled")
    }
}
package com.bignerdranch.android.photogallery

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.bignerdranch.android.photogallery.api.FlickrResponse
import com.bignerdranch.android.photogallery.api.PhotoResponse
import retrofit2.Call
import kotlinx.coroutines.flow.Flow
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import android.app.Application
import androidx.lifecycle.AndroidViewModel

class PhotoGalleryViewModel(private val app: Application): AndroidViewModel(app) {
    val galleryItemLiveData: LiveData<List<GalleryItem>>
//    val usersFlow: Flow<PagingData<GalleryItem>>

    private val flickrFetchr = FlickrFetchr()
    private val mutableSearchTerm = MutableLiveData<String>()

    init {
        mutableSearchTerm.value = QueryPreferences.getStoredQuery(app)
//        galleryItemLiveData = FlickrFetchr().searchPhotos("car")

        galleryItemLiveData = Transformations.switchMap(mutableSearchTerm) {
            searchTerm -> if (searchTerm.isBlank()) {
                flickrFetchr.fetchPhotos()
              } else flickrFetchr.searchPhotos(searchTerm)
        }
        Log.d("PhotoGalleryViewModel", "New ViewModel")
//        usersFlow = a
    }

    fun fetchPhotos(query: String = "") {
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
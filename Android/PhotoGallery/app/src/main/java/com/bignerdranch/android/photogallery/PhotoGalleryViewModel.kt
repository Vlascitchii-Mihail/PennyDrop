package com.bignerdranch.android.photogallery

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.bignerdranch.android.photogallery.api.FlickrResponse
import com.bignerdranch.android.photogallery.api.PhotoResponse
import retrofit2.Call
import kotlinx.coroutines.flow.Flow

class PhotoGalleryViewModel: ViewModel() {
    val galleryItemLiveData: LiveData<List<GalleryItem>>
//    val usersFlow: Flow<PagingData<GalleryItem>>

    init {
        galleryItemLiveData = FlickrFetchr().fetchPhotos()
        Log.d("PhotoGalleryViewModel", "New ViewModel")
//        usersFlow = a
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
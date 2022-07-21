package com.bignerdranch.android.photogallery

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import com.bignerdranch.android.photogallery.api.FlickrResponse
import com.bignerdranch.android.photogallery.api.PhotoResponse
import retrofit2.Call

class PhotoGalleryViewModel: ViewModel() {
    val galleryItemLiveData: LiveData<List<GalleryItem>>

    init {
        galleryItemLiveData = FlickrFetchr().fetchPhotos()
        Log.d("PhotoGalleryViewModel", "New ViewModel")
    }

    companion object {
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
        Log.d("PhotoGalleryViewModel", "Canceled")
    }
}
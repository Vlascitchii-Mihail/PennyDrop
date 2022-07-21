package com.bignerdranch.android.photogallery

import com.bignerdranch.android.photogallery.api.FlickrApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import android.util.Log
import com.bignerdranch.android.photogallery.api.FlickrResponse
import com.bignerdranch.android.photogallery.api.PhotoDeserializer
import com.bignerdranch.android.photogallery.api.PhotoResponse
import retrofit2.Response
import com.google.gson.GsonBuilder

private const val TAG = "FlickrFetchr"

class FlickrFetchr {
    private val flickrApi: FlickrApi

    init {
        val gsonPhotoDeserializer = GsonBuilder()
            .registerTypeAdapter(PhotoResponse::class.java, PhotoDeserializer()).create()

        val retrofit: Retrofit = Retrofit.Builder().baseUrl("https://api.flickr.com/").
        addConverterFactory(GsonConverterFactory.create(gsonPhotoDeserializer)).build()
        flickrApi = retrofit.create(FlickrApi::class.java)
    }

    fun fetchPhotos(): MutableLiveData<List<GalleryItem>> {
        val responseLiveData: MutableLiveData<List<GalleryItem>> = MutableLiveData()
        val flickrRequest: Call<PhotoResponse> = flickrApi.fetchPhotos()
//        PhotoGalleryViewModel.fll(flickrRequest)

        flickrRequest.enqueue(object: Callback<PhotoResponse> {
            override fun onFailure(call: Call<PhotoResponse>, t: Throwable) {
                Log.e(TAG, "Failed to fetch photos", t)
            }

            override fun onResponse(call: Call<PhotoResponse>, response: Response<PhotoResponse>) {
                Log.d(TAG, "Response received")
//                responseLiveData.value = response.body()

                val photoResponse: PhotoResponse? = response.body()
//                val photoResponse: PhotoResponse? = flickrResponse?.photos
                var galleryItems: List<GalleryItem> = photoResponse?.galleryItems ?: mutableListOf()
                galleryItems = galleryItems.filterNot { it.url.isBlank() }
                responseLiveData.value = galleryItems
            }
        })

        return responseLiveData
    }

}
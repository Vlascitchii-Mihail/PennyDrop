package com.bignerdranch.android.photogallery

import com.bignerdranch.android.photogallery.api.FlickrApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.bignerdranch.android.photogallery.api.FlickrResponse
import com.bignerdranch.android.photogallery.api.PhotoDeserializer
import com.bignerdranch.android.photogallery.api.PhotoResponse
import retrofit2.Response
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.Flow
import androidx.annotation.WorkerThread
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import okhttp3.ResponseBody


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

    fun getPageGallery(photoResponse: PhotoResponse): Flow<PagingData<GalleryItem>> {
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false,
                 initialLoadSize = 10),
            pagingSourceFactory = {PagingPhotoSource(photoResponse)}
        ).flow
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

                val photoResponse: PhotoResponse = response.body() ?: PhotoResponse()
                Log.d(TAG, "page = ${photoResponse.getPage()} and ${photoResponse.getPages()} at all")

                PhotoGalleryViewModel.flow = getPageGallery(photoResponse)

                var galleryItems: List<GalleryItem> = photoResponse.galleryItems
                galleryItems = galleryItems.filterNot { it.url.isBlank() }
                responseLiveData.value = galleryItems
            }
        })

        return responseLiveData
    }

    @WorkerThread fun fetchPhoto(url: String): Bitmap? {
        val response : Response<ResponseBody> = flickrApi.fetchUrlBytes(url).execute()
        val bitmap = response.body()?.byteStream()?.use(BitmapFactory::decodeStream)
        Log.i(TAG, "Decoded bitmap = $bitmap from Response = $response")
        return bitmap
    }

}
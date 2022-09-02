package com.bignerdranch.android.photogallery

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
import retrofit2.Response
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.Flow
import androidx.annotation.WorkerThread
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.bignerdranch.android.photogallery.api.*
import okhttp3.ResponseBody
import okhttp3.OkHttpClient


private const val TAG = "FlickrFetchr"

class FlickrFetchr {
    private val flickrApi: FlickrApi

    init {

        //creating JSon custom deserializer - deserialize web request to PhotoResponse::class.java
        val gsonPhotoDeserializer = GsonBuilder()

                //registerTypeAdapter() - Configures Gson for custom serialization or deserialization.
            .registerTypeAdapter(PhotoResponse::class.java, PhotoDeserializer()).create()

        //creating a new requests' interceptor
        val client = OkHttpClient.Builder().addInterceptor(PhotoInterceptor()).build()

        //creating Retrofit's exemplar
        //Retrofit.Builder() - interface which creates and builds Retrofit's exemplar
        //baseUrl() - setting base API URL address
        //addConverterFactory() - adding converter
        //build() - returns Retrofit's object
        val retrofit: Retrofit = Retrofit.Builder().baseUrl("https://api.flickr.com/").

            //adding custom deserializer in the Retrofit's object
            //client(client) - adding query interceptor
        addConverterFactory(GsonConverterFactory.create(gsonPhotoDeserializer)).build()

        //retrofit.create() - using Retrofit's object to create an exemplar of FlickrApi's interface
        flickrApi = retrofit.create(FlickrApi::class.java)
    }

    //creating web request - object Call
    fun fetchPhotosRequest(): Call<PhotoResponse> {

        //fetchPhotos returns Call<PhotoResponse> request
        return flickrApi.fetchPhotos()
    }

    fun searchPhotoRequest(query: String): Call<PhotoResponse> {

        //searching photos
        return flickrApi.searchPhotos(query)
    }

    fun getPageGallery(photoResponse: PhotoResponse): Flow<PagingData<GalleryItem>> {
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false,
                 initialLoadSize = 10),
            pagingSourceFactory = {PagingPhotoSource(photoResponse)}
        ).flow
    }



    fun searchPhotos(query: String): LiveData<List<GalleryItem>> {
//        return fetchPhotoMetadata(flickrApi.searchPhotos(query))
        return fetchPhotoMetadata(searchPhotoRequest(query))
    }

    fun fetchPhotos(): LiveData<List<GalleryItem>> {
//        return fetchPhotoMetadata(flickrApi.fetchPhotos())
        return fetchPhotoMetadata(fetchPhotosRequest())
    }

    //flickrRequest - web request, type Call
    private fun fetchPhotoMetadata(flickrRequest: Call<PhotoResponse>): LiveData<List<GalleryItem>> {

        //LiveData object for receiving results from PhotoGalleryFragment
        val responseLiveData: MutableLiveData<List<GalleryItem>> = MutableLiveData()
//        val flickrRequest: Call<PhotoResponse> = flickrApi.fetchPhotos()
//        PhotoGalleryViewModel.fll(flickrRequest)

        //enqueue() - executes web request in background thread to a website using Call
        flickrRequest.enqueue(object: Callback<PhotoResponse> {

            //runs if we don't receive response from a server
            override fun onFailure(call: Call<PhotoResponse>, t: Throwable) {
                Log.e(TAG, "Failed to fetch photos", t)
            }

            //runs if we received response from a server
            //response: Response<PhotoResponse> - response from a server
            override fun onResponse(call: Call<PhotoResponse>, response: Response<PhotoResponse>) {
                Log.d(TAG, "Response received")
//                responseLiveData.value = response.body()

                //response.body() - response from a server, which returns PhotoResponse type
                val photoResponse: PhotoResponse = response.body() ?: PhotoResponse()
                Log.d(TAG, "page = ${photoResponse.getPage()} and ${photoResponse.getPages()} at all")

                PhotoGalleryViewModel.flow = getPageGallery(photoResponse)

                var galleryItems: List<GalleryItem> = photoResponse.galleryItems

                //filter photos from Flickr request without url address
                galleryItems = galleryItems.filterNot { it.url.isBlank() }
                responseLiveData.value = galleryItems
            }
        })

        //immutable return type
        //others classes can create FlickrFetchr exemplar to get access to LiveData's object
        return responseLiveData
    }

    //download data from the URL
    //@WorkerThread - required executing in background thread
    @WorkerThread fun fetchPhoto(url: String): Bitmap? {
        val response : Response<ResponseBody> = flickrApi.fetchUrlBytes(url).execute()

        //byteStream() - returns InputStream - stream of bytes
        //use() - Выполняет заданную функцию блока на этом ресурсе, а затем корректно закрывает его, независимо от того, выдано исключение или нет.
        //BitmapFactory::decodeStream - reference to the method decodeStream() in BitmapFactory
        //BitmapFactory::decodeStream - creates Bitmap from InputStream's data
        val bitmap = response.body()?.byteStream()?.use(BitmapFactory::decodeStream)
        Log.i(TAG, "Decoded bitmap = $bitmap from Response = $response")
        return bitmap
    }

}
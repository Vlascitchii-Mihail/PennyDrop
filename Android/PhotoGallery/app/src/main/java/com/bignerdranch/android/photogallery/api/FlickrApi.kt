package com.bignerdranch.android.photogallery.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url
import retrofit2.http.Query

interface FlickrApi {
    //0de99c04241cebbb57e0571d117e351b
    @GET("services/rest/?method=flickr.interestingness.getList" +
            "&api_key=0de99c04241cebbb57e0571d117e351b" +
            "&format=json" +
            "&nojsoncallback=1" +
            "&extras=url_s" +
            "&per_page=500")
    fun fetchPhotos(): Call<PhotoResponse>

    @GET fun fetchUrlBytes(@Url url: String): Call<ResponseBody>

    @GET("services/rest?method=flickr.photos.search" +
            "&api_key=0de99c04241cebbb57e0571d117e351b" +
            "&format=json" +
            "&nojsoncallback=1" +
            "&extras=url_s" +
            "&per_page=500")
    fun searchPhotos(@Query("text") query: String): Call<PhotoResponse>
}
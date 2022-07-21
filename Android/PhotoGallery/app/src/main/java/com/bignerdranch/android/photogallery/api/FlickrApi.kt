package com.bignerdranch.android.photogallery.api

import retrofit2.Call
import retrofit2.http.GET

interface FlickrApi {
    //0de99c04241cebbb57e0571d117e351b
    @GET("services/rest/?method=flickr.interestingness.getList" +
            "&api_key=0de99c04241cebbb57e0571d117e351b" +
            "&format=json" +
            "&nojsoncallback=1" +
            "&extras=url_s")
    fun fetchPhotos(): Call<PhotoResponse>
}
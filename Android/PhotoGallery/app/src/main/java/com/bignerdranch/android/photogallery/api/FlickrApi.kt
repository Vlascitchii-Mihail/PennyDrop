package com.bignerdranch.android.photogallery.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url
import retrofit2.http.Query

interface FlickrApi {

    //annotation tels to Retrofit type of the query
    //retrofit2.Call - return type of query
    //Call - web query for website
    //0de99c04241cebbb57e0571d117e351b
    //services/rest/?method=flickr.interestingness - part of the URL's way
    //Call<PhotoResponse> = Call deserializes on PhotoResponse
    //getList() - Rest API function from Flickr website
    //services/rest - second part of URL address
    @GET("services/rest/?method=flickr.interestingness.getList" +

            //my API key
            "&api_key=0de99c04241cebbb57e0571d117e351b" +
            "&format=json" +

            //1 - remove round brackets () from response
            "&nojsoncallback=1" +

            //url_s - adds url address for each image
            "&extras=url_s" +
            "&per_page=500")

    //fetchPhotos returns Call<PhotoResponse> request
    fun fetchPhotos(): Call<PhotoResponse>

    //get the URL address downloads and returns data from the URL
    @GET fun fetchUrlBytes(@Url url: String): Call<ResponseBody>

    @GET("services/rest?method=flickr.photos.search" +
            "&api_key=0de99c04241cebbb57e0571d117e351b" +
            "&format=json" +
            "&nojsoncallback=1" +
            "&extras=url_s" +
            "&per_page=500")
    fun searchPhotos(@Query("text") query: String): Call<PhotoResponse>
}
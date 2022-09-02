package com.bignerdranch.android.photogallery.api

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.HttpUrl

//import retrofit2.Response
//import android.app.DownloadManager.Request

private const val API_KEY = "0de99c04241cebbb57e0571d117e351b"

//intercepts Flickr's API request or an answer and modifies him
class PhotoInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        //chain.request() - access to the initial query
        val originalRequest: Request = chain.request()

        //originalRequest.url() - extracting an URL from the query
        //newBuilder() - adding new parameters
        val newUrl: HttpUrl = originalRequest.url().newBuilder()
            .addQueryParameter("api_key", API_KEY)
            .addQueryParameter("format", "json")
            .addQueryParameter("nojsoncallback", "1")
            .addQueryParameter("extras", "url_s")
            .addQueryParameter("safesearch", "1").build()

        val newRequest: Request = originalRequest.newBuilder().url(newUrl).build()

        return chain.proceed(newRequest)
    }
}
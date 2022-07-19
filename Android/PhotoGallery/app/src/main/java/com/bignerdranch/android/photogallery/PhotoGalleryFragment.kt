package com.bignerdranch.android.photogallery

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.bignerdranch.android.photogallery.api.FlickrApi
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
private const val TAG = "PhotoGalleryFragment"

class PhotoGalleryFragment: Fragment() {
    private lateinit var photoRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val flickrHomePageRequest: Call<String> = flickrApi.fetchContents()
//        flickrHomePageRequest.enqueue(object: Callback<String> {
//            override fun onFailure(call: Call<String>, t: Throwable) {
//                Log.e(TAG, "Failed to fetch photos")
//            }
//
//            override fun onResponse(call: Call<String>, response: Response<String>) {
//                Log.d(TAG, "Response received: ${response.body()}")
//            }
//        })

        val flickrLiveData: LiveData<List<GalleryItem>> = FlickrFetch().fetchPhotos()
        flickrLiveData.observe(this, Observer { galleryItems ->
            Log.d(TAG, "Response received: $galleryItems")
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_photo_gallery, container, false)

        photoRecyclerView = view.findViewById(R.id.photo_recycler_view)
        photoRecyclerView.layoutManager = GridLayoutManager(context, 3)

        return view
    }

    companion object {
        fun newInstance() = PhotoGalleryFragment()
    }
}
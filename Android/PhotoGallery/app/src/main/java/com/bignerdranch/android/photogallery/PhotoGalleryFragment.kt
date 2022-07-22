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
import android.widget.Adapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "PhotoGalleryFragment"

class PhotoGalleryFragment: Fragment() {
    private lateinit var photoRecyclerView: RecyclerView
    private val photoGalleryViewModel: PhotoGalleryViewModel by lazy {
        ViewModelProvider(this)[PhotoGalleryViewModel::class.java]
    }

//    private lateinit var photoGalleryViewModel: PhotoGalleryViewModel

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

//        val flickrLiveData: LiveData<List<GalleryItem>> = FlickrFetch().fetchPhotos()
//        flickrLiveData.observe(this, Observer { galleryItems ->
//            Log.d(TAG, "Response received: $galleryItems")
//        })

//        photoGalleryViewModel = ViewModelProvider(this)[PhotoGalleryViewModel::class.java]
//        photoGalleryViewModel = ViewModelProvider(this).get(PhotoGalleryViewModel::class.java)
//        photoGalleryViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(PhotoGalleryViewModel::class.java)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_photo_gallery, container, false)

        photoRecyclerView = view.findViewById(R.id.photo_recycler_view)
        photoRecyclerView.layoutManager = GridLayoutManager(context, 3)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        Log.d(TAG, "Not Canceled")
        photoGalleryViewModel.galleryItemLiveData.observe(
            viewLifecycleOwner, Observer { galleryItems ->
//                Log.d(TAG, "Have gallery items from ViewModel $galleryItems")
                val adapter = PhotoAdapter(galleryItems)
                photoRecyclerView.adapter = adapter
                observeGallery(adapter)
            }
        )
//        Log.d(TAG, "Have gallery items from ViewModel ${photoGalleryViewModel.galleryItemLiveData.value.toString()}")


    }

    private class PhotoHolder(itemTextView: TextView): RecyclerView.ViewHolder(itemTextView) {
        val bindTitle: (CharSequence) -> Unit = itemTextView::setText
    }

    private class PhotoAdapter(private val galleryItems: List<GalleryItem>): PagingDataAdapter<GalleryItem, PhotoHolder>(GalleryDiffCallback()) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
            val textView = TextView(parent.context)
            return  PhotoHolder (textView)
        }

        override fun getItemCount(): Int = galleryItems.size

        override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
            val galleryItem = galleryItems[position]
            holder.bindTitle(galleryItem.title)
        }
    }

    companion object {
        fun newInstance() = PhotoGalleryFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        PhotoGalleryViewModel.cancelCall()
        Log.d(TAG, "Canceled")
    }

    private fun observeGallery(adapter: PhotoAdapter) {
        viewLifecycleOwner.lifecycleScope.launch {
            PhotoGalleryViewModel.flow.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }
    }

}

class GalleryDiffCallback: DiffUtil.ItemCallback<GalleryItem>() {
    override fun areItemsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean {
        return oldItem == newItem
    }
}
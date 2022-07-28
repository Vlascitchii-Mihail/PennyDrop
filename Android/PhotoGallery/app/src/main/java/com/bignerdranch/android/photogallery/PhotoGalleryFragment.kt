package com.bignerdranch.android.photogallery

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import android.widget.ImageView
import android.graphics.drawable.Drawable
import android.os.Handler
import androidx.core.content.ContextCompat

private const val TAG = "PhotoGalleryFragment"

class PhotoGalleryFragment: Fragment() {
    private lateinit var photoRecyclerView: RecyclerView
    private val photoGalleryViewModel: PhotoGalleryViewModel by lazy {
        ViewModelProvider(this)[PhotoGalleryViewModel::class.java]
    }

    private lateinit var thumbnailDownloader: ThumbnailDownloader<PhotoHolder>

//    private lateinit var photoGalleryViewModel: PhotoGalleryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

//        photoGalleryViewModel = ViewModelProvider(this)[PhotoGalleryViewModel::class.java]
//        photoGalleryViewModel = ViewModelProvider(this).get(PhotoGalleryViewModel::class.java)
//        photoGalleryViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(PhotoGalleryViewModel::class.java)

        val responseHandler = Handler()
        thumbnailDownloader = ThumbnailDownloader(responseHandler) {photoHolder, bitmap ->
            val drawable = BitmapDrawable(resources, bitmap)
            photoHolder.bindDrawable(drawable)
        }
        lifecycle.addObserver(thumbnailDownloader.fragmentLifecycleObserver)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewLifecycleOwner.lifecycle.addObserver(thumbnailDownloader)
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

    private class PhotoHolder(private val itemImageView: ImageView): RecyclerView.ViewHolder(itemImageView) {
        val bindDrawable: (Drawable) -> Unit = itemImageView::setImageDrawable
    }

    private inner class PhotoAdapter(private val galleryItems: List<GalleryItem>): PagingDataAdapter<GalleryItem, PhotoHolder>(GalleryDiffCallback()) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
            val view = layoutInflater.inflate(R.layout.list_item_gallery, parent, false) as ImageView
            return PhotoHolder(view)
        }

        override fun getItemCount(): Int = galleryItems.size

        override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
            val galleryItem = galleryItems[position]
            val placeholder: Drawable = ContextCompat.getDrawable(requireContext(), R.drawable.bill_up_close) ?: ColorDrawable()
            holder.bindDrawable(placeholder)
            thumbnailDownloader.queueThumbnail(holder, galleryItem.url)
        }
    }

    companion object {
        fun newInstance() = PhotoGalleryFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewLifecycleOwner.lifecycle.removeObserver(
            thumbnailDownloader.viewLifecycleObserver
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        PhotoGalleryViewModel.cancelCall()
        Log.d(TAG, "Canceled")

        lifecycle.removeObserver(thumbnailDownloader.fragmentLifecycleObserver)
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
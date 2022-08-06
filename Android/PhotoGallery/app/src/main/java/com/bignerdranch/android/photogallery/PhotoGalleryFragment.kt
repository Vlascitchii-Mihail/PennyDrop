package com.bignerdranch.android.photogallery

import android.app.ProgressDialog
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
import android.os.Looper
import androidx.core.content.ContextCompat
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.work.*
import com.bignerdranch.android.photogallery.api.PollWorker
import java.util.concurrent.TimeUnit
import android.content.Intent

private const val TAG = "PhotoGalleryFragment"
private const val POLL_WORK = "POLL_WORK"

class PhotoGalleryFragment: VisibleFragment() {
    private lateinit var photoRecyclerView: RecyclerView
    private val photoGalleryViewModel: PhotoGalleryViewModel by lazy {
        ViewModelProvider(this)[PhotoGalleryViewModel::class.java]
    }

    private lateinit var thumbnailDownloader: ThumbnailDownloader<PhotoHolder>

//    private lateinit var photoGalleryViewModel: PhotoGalleryViewModel

//    private lateinit var progressDialog: ProgressDialog
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        setHasOptionsMenu(true)

//        progressDialog = ProgressDialog(requireContext())
//        progressDialog.setTitle("Downloading photos")
//        progressDialog.setMessage("It might take a few seconds...")
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)


//        photoGalleryViewModel = ViewModelProvider(this)[PhotoGalleryViewModel::class.java]
//        photoGalleryViewModel = ViewModelProvider(this).get(PhotoGalleryViewModel::class.java)
//        photoGalleryViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(PhotoGalleryViewModel::class.java)

        val responseHandler = Handler(Looper.getMainLooper())
        thumbnailDownloader = ThumbnailDownloader(responseHandler) {photoHolder, bitmap ->
            val drawable = BitmapDrawable(resources, bitmap)
            photoHolder.bindDrawable(drawable)
        }
        lifecycle.addObserver(thumbnailDownloader.fragmentLifecycleObserver)

//        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.UNMETERED).build()

//        val workRequest = OneTimeWorkRequest.Builder(PollWorker::class.java).setConstraints(constraints).build()
//        WorkManager.getInstance(requireContext()).enqueue(workRequest)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewLifecycleOwner.lifecycle.addObserver(thumbnailDownloader)
        val view = inflater.inflate(R.layout.fragment_photo_gallery, container, false)

        photoRecyclerView = view.findViewById(R.id.photo_recycler_view)
        photoRecyclerView.layoutManager = GridLayoutManager(context, 3)

        progressBar = view.findViewById(R.id.my_progress_bar)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        Log.d(TAG, "Not Canceled")
        photoGalleryViewModel.galleryItemLiveData.observe(
            viewLifecycleOwner, Observer { galleryItems ->
//                Log.d(TAG, "Have gallery items from ViewModel $galleryItems")
                progressBar.isVisible = false
                val adapter = PhotoAdapter(galleryItems)
                photoRecyclerView.adapter = adapter
                observeGallery(adapter)
            }
        )
//        Log.d(TAG, "Have gallery items from ViewModel ${photoGalleryViewModel.galleryItemLiveData.value.toString()}")


    }

    private inner class PhotoHolder(private val itemImageView: ImageView): RecyclerView.ViewHolder(itemImageView), View.OnClickListener {
        private lateinit var galleryItem: GalleryItem

        init {
            itemView.setOnClickListener(this)
        }

        val bindDrawable: (Drawable) -> Unit = itemImageView::setImageDrawable

        fun bindGalleryItem(item: GalleryItem) {
            galleryItem = item
        }

        override fun onClick(view: View) {
            //starting browser
//            val intent = Intent(Intent.ACTION_VIEW, galleryItem.photoPageUri)
//            startActivity(intent)

            val intent = PhotoPageActivity.newInstance(requireContext(), galleryItem.photoPageUri)
            startActivity(intent)
        }
    }

    private inner class PhotoAdapter(private val galleryItems: List<GalleryItem>): PagingDataAdapter<GalleryItem, PhotoHolder>(GalleryDiffCallback()) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
            val view = layoutInflater.inflate(R.layout.list_item_gallery, parent, false) as ImageView
            return PhotoHolder(view)
        }

        override fun getItemCount(): Int = galleryItems.size

        override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
            val galleryItem = galleryItems[position]
            holder.bindGalleryItem(galleryItem)
//            val placeholder: Drawable = ContextCompat.getDrawable(requireContext(), R.drawable.bill_up_close) ?: ColorDrawable()
//            holder.bindDrawable(placeholder)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_photo_gallery, menu)

        val searchItem: MenuItem = menu.findItem(R.id.menu_item_search)
        val searchView = searchItem.actionView as SearchView

        searchView.apply {
            setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(queryText: String): Boolean {
                    Log.d(TAG, "QueryTextSubmit: $queryText")
                    progressBar.isVisible = true
                    progressBar
                    photoGalleryViewModel.fetchPhotos(queryText)
                    clearFocus()
                    return true
                }

                override fun onQueryTextChange(queryText: String): Boolean {
                    Log.d(TAG, "QueryTextChange: $queryText")
                    return false
                }
            })

            setOnSearchClickListener {
                searchView.setQuery(photoGalleryViewModel.searchTerm, false)
            }

            setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus) searchItem.collapseActionView()
            }
        }

        val toggleItem = menu.findItem(R.id.menu_item_toggle_polling)
        val isPolling = QueryPreferences.isPolling(requireContext())
        val toggleItemTitle = if (isPolling) {
            R.string.stop_polling
        } else R.string.start_polling
        toggleItem.setTitle(toggleItemTitle)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.menu_item_clear -> { photoGalleryViewModel.fetchPhotos("")
            true }
            R.id.menu_item_toggle_polling -> {
                val isPolling = QueryPreferences.isPolling(requireContext())
                if (isPolling) {
                    WorkManager.getInstance(requireContext()).cancelUniqueWork(POLL_WORK)
                    QueryPreferences.setPolling(requireContext(), false)
                } else {
                    val constraints = Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.UNMETERED).build()

                    val periodicRequest = PeriodicWorkRequest.
                    Builder(PollWorker::class.java, 15, TimeUnit.MINUTES)
                        .setConstraints(constraints).build()

                    WorkManager.getInstance(requireContext())
                        .enqueueUniquePeriodicWork(POLL_WORK, ExistingPeriodicWorkPolicy.KEEP, periodicRequest)

                    QueryPreferences.setPolling(requireContext(), true)
                }
                activity?.invalidateOptionsMenu()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
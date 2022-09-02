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
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.browser.customtabs.CustomTabsIntent

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

    //creating a progress bar's variable
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //saving the fragment before changing the orientation
        retainInstance = true

        //fragment's registration for getting a callbacks from the Menu
        setHasOptionsMenu(true)

//        progressDialog = ProgressDialog(requireContext())
//        progressDialog.setTitle("Downloading photos")
//        progressDialog.setMessage("It might take a few seconds...")
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)


//        photoGalleryViewModel = ViewModelProvider(this)[PhotoGalleryViewModel::class.java]
//        photoGalleryViewModel = ViewModelProvider(this).get(PhotoGalleryViewModel::class.java)
//        photoGalleryViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(PhotoGalleryViewModel::class.java)

        //creating a new Handler's object which is connected to UI thread
        val responseHandler = Handler(Looper.getMainLooper())
        thumbnailDownloader = ThumbnailDownloader(responseHandler) { photoHolder, bitmap ->
            val drawable = BitmapDrawable(resources, bitmap)
            photoHolder.bindDrawable(drawable) }

        /**
         * @param lifecycle - returns a LifeCycle of the Fragment
         * @param addObserver() - Adds a LifecycleObserver that will be notified when the LifecycleOwner changes state.
         */
        lifecycle.addObserver(thumbnailDownloader.fragmentLifecycleObserver)

//        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.UNMETERED).build()

//        val workRequest = OneTimeWorkRequest.Builder(PollWorker::class.java).setConstraints(constraints).build()
//        WorkManager.getInstance(requireContext()).enqueue(workRequest)


//        activity?.onBackPressedDispatcher?.addCallback(this, object: OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                Toast.makeText(context, "Button BACK clicked", Toast.LENGTH_SHORT).show()
//                Log.d(TAG, "Button BACK clicked")
//            }
//        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewLifecycleOwner.lifecycle.addObserver(thumbnailDownloader.viewLifecycleObserver)
        val view = inflater.inflate(R.layout.fragment_photo_gallery, container, false)

        //setting Recycler View
        photoRecyclerView = view.findViewById(R.id.photo_recycler_view)

        //setting grid scale of the Recycler View
        /**
         * @param 3 - columns quantity
         * @param context - Return the Context this fragment is currently associated with
         */
        photoRecyclerView.layoutManager = GridLayoutManager(context, 3)

        progressBar = view.findViewById(R.id.my_progress_bar)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        Log.d(TAG, "Not Canceled")

        //listening for LiveData with photo's list
        photoGalleryViewModel.galleryItemLiveData.observe(

            //viewLifecycleOwner = lifeCycleOwner object, when fragment disconnects, viewLifecycleOwner destroys and listener disappears
            viewLifecycleOwner, Observer { galleryItems ->
//                Log.d(TAG, "Have gallery items from ViewModel $galleryItems")

                //hiding the progressBar when the photos is shown
                progressBar.isVisible = false

                //sending to the adapter photo's list
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

        //set drawable to itemImageView
        val bindDrawable: (Drawable) -> Unit = itemImageView::setImageDrawable

        fun bindGalleryItem(item: GalleryItem) {
            galleryItem = item
        }

        override fun onClick(view: View) {
            //starting browser
//            val intent = Intent(Intent.ACTION_VIEW, galleryItem.photoPageUri)
//            startActivity(intent)

            val intent = PhotoPageActivity.newIntent(requireContext(), galleryItem.photoPageUri)
            startActivity(intent)

            CustomTabsIntent.Builder().setToolbarColor(ContextCompat.getColor(requireContext(), R.color.black))
                .setShowTitle(true).build().launchUrl(requireContext(), galleryItem.photoPageUri)
        }
    }

    // inner  - supplies parent layoutInflater
    private inner class PhotoAdapter(private val galleryItems: List<GalleryItem>): PagingDataAdapter<GalleryItem, PhotoHolder>(GalleryDiffCallback()) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {

            //creating image view
            val view = layoutInflater.inflate(R.layout.list_item_gallery, parent, false) as ImageView
            return PhotoHolder(view)
        }

        override fun getItemCount(): Int = galleryItems.size

        override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
            val galleryItem = galleryItems[position]
            holder.bindGalleryItem(galleryItem)

            //getting drawable from R.drawable
//            val placeholder: Drawable = ContextCompat.getDrawable(requireContext(), R.drawable.bill_up_close) ?: ColorDrawable()
//            holder.bindDrawable(placeholder)

            //starting a new thread
            thumbnailDownloader.queueThumbnail(holder, galleryItem.url)
        }
    }

    //creating a new PhotoGalleryFragment's object in other classes
    companion object {
        fun newInstance() = PhotoGalleryFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        //removing a LifecycleListener from ThumbnailDownloader
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

    //inflating the Menu's widget
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_photo_gallery, menu)

        //search window
        val searchItem: MenuItem = menu.findItem(R.id.menu_item_search)

        //extracting a searchView from searchItem
        val searchView = searchItem.actionView as SearchView

        searchView.apply {

            //Sets a listener for user actions within the SearchView
            setOnQueryTextListener(object: SearchView.OnQueryTextListener {

                //Called when the user submits (sends) the query. This could be due to a key
                // press on the keyboard or due to pressing a submit button.
                override fun onQueryTextSubmit(queryText: String): Boolean {
                    Log.d(TAG, "QueryTextSubmit: $queryText")

                    //showing the progressBar when user sends query
                    progressBar.isVisible = true
//                    progressBar
                    photoGalleryViewModel.fetchPhotos(queryText)
                    clearFocus()

                    //true - query was processed
                    return true
                }

                //Called when the query text is changed by the user
                override fun onQueryTextChange(queryText: String): Boolean {
                    Log.d(TAG, "QueryTextChange: $queryText")

                    //false - we don't handle the text changes
                    return false
                }
            })

            //setting the query when the SearchView is clicked
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

    //This hook is called whenever an item in your options menu is selected.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        //some menu item's ID
        return when(item.itemId) {

            //clear button of the menu
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
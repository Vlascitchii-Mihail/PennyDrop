package com.bignerdranch.android.photogallery

import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import androidx.lifecycle.*
import java.util.concurrent.ConcurrentHashMap
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Message
import retrofit2.http.OPTIONS

private const val TAG = "ThumbnailDownloader"
private const val MESSAGE_DOWNLOAD = 0

//downloading and transfer data to PhotoGalleryFragment
/**
 * @param T - identifier of each download operation
 * @param responseHandler: Handler - the Handler from the main(UI) threat
 */
class ThumbnailDownloader<in T>(
    private val responseHandler: Handler, private val onThumbnailDownloaded: (
        T, Bitmap) -> Unit) : HandlerThread(TAG), DefaultLifecycleObserver {

    private var hasQuit = false

    //will be contain the reference to the Handler
    private lateinit var requestHandler: Handler

    //thread-safe HashMap
    private val requestMap = ConcurrentHashMap<T, String>()

    //creating a new FlickrFetchr's object
    private val flickerFetchr = FlickrFetchr()

    //executing the function setup() when was received callback from LifecycleOwner
    //    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
//    fun setup() {
//        Log.i(TAG, "Starting background thread")
//    }
//
    //executing the function tearDown() when was received callback from LifecycleOwner
//    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
//    fun tearDown() {
//        Log.i(TAG, "Destroying background thread")
//    }

//    private fun setup() {
//        Log.i(TAG, "Starting background thread")
//        start()
//        looper
//    }

//    private fun tearDown() {
//        Log.i(TAG, "Destroying background thread")
//        quit()
//    }

    //adding the LifecycleObserver's interface, fun onCreate() listener
    //receiving the callbacks from the owner: LifecycleOwner fun onCreate()
    val fragmentLifecycleObserver: LifecycleObserver = object: DefaultLifecycleObserver {
        override fun onCreate(owner: LifecycleOwner) {
            Log.i(TAG, "Starting background thread")

            //starting a new thread
            start()
            looper
        }

        //adding the LifecycleObserver's interface, fun onDestroy() listener
        //receiving the callbacks from the owner: LifecycleOwner fun onDestroy()
        override fun onStop(owner: LifecycleOwner) {
            Log.i(TAG, "Destroying background thread")
            requestHandler.removeMessages(MESSAGE_DOWNLOAD)
            requestMap.clear()

            //quit from the background thread
            quit()
        }
    }

    //adding the LifecycleObserver's interface, fun onDestroy() listener
    //receiving the callbacks from the owner: LifecycleOwner fun onDestroy()
    val viewLifecycleObserver: LifecycleObserver = object: DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            Log.i(TAG, "Clearing all requests from queue")
            requestHandler.removeMessages(MESSAGE_DOWNLOAD)
            requestMap.clear()
        }
    }


    override fun quit(): Boolean {
        hasQuit = true
        return super.quit()
    }

    /**
     * @param target: T - download's ID
     */
    fun queueThumbnail(target: T, url: String) {
        Log.i(TAG, "Got a Url: $url")
        requestMap[target] = url

        //creating a new request for downloading a image
        // requestHandler - Handler
        //obtainMessage() - creation a new Message
        //sendToTarget() - sending a message to the handler
        /**
         * @param MESSAGE_DOWNLOAD - CONSTANT Int- identification of messages
         * @param target - download's ID
         */
        requestHandler.obtainMessage(MESSAGE_DOWNLOAD, target).sendToTarget()
    }

    //fun onLooperPrepared() вызываетсф до того, как Looper проверит очередь
    //вызывается когда сообщение извлечено и готово к обработке
    @Suppress("UNCHECKED_CAST")
    @SuppressLint("handlerLeak")
    override fun onLooperPrepared() {

        //creating a new Handler's object
        requestHandler = object: Handler() {
            override fun handleMessage(msg: Message) {
                if (msg.what == MESSAGE_DOWNLOAD) {

                    /**
                     * @param msg.obj - identifier of the request
                     */
                    val target = msg.obj as T
                    Log.i(TAG, "Got a request for URl: ${requestMap[target]}")

                    //downloading the photos
                    handleRequest(target)
                }
            }
        }
    }

    //downloading the photos
    private fun handleRequest(target: T) {
        val url = requestMap[target] ?: return

        //downloading the photos from the Retrofit
        val bitmap = flickerFetchr.fetchPhoto(url) ?: return

        //sending a message to the UI thread\
        //Runnable executes in the main thread
        //post() - sending for executing the message to the UI thread
        responseHandler.post(Runnable {

            //if Recycler View didn't redesigned the Handler
            //hasQuit - ThumbnailDownloader  hasQuit state
            if (requestMap[target] != url || hasQuit) return@Runnable
            requestMap.remove(target)
            onThumbnailDownloaded(target, bitmap)
        })
    }
}
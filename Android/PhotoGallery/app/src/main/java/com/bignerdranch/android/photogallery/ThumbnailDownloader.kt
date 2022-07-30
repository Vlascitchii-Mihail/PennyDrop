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

class ThumbnailDownloader<in T>(
    private val responseHandler: Handler, private val onThumbnailDownloaded: (
        T, Bitmap) -> Unit) : HandlerThread(TAG), DefaultLifecycleObserver {

    private var hasQuit = false
    private lateinit var requestHandler: Handler
    private val requestMap = ConcurrentHashMap<T, String>()
    private val flickerFetchr = FlickrFetchr()

    val fragmentLifecycleObserver: LifecycleObserver = object: DefaultLifecycleObserver {
        override fun onCreate(owner: LifecycleOwner) {
            Log.i(TAG, "Starting background thread")
            start()
            looper
        }

        override fun onDestroy(owner: LifecycleOwner) {
            Log.i(TAG, "Destroying background thread")
            quit()
        }
    }

    val viewLifecycleObserver: LifecycleObserver = object: DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            Log.i(TAG, "Clearing all requests from queue")
            requestHandler.removeMessages(MESSAGE_DOWNLOAD)
            requestMap.clear()
        }
    }

    @Suppress("UNCHECKED_CAST")
    @SuppressLint("handlerLeak")
    override fun onLooperPrepared() {
        requestHandler = object: Handler() {
            override fun handleMessage(msg: Message) {
                if (msg.what == MESSAGE_DOWNLOAD) {
                    val target = msg.obj as T
                    Log.i(TAG, "Got a request for URl: ${requestMap[target]}")
                    handleRequest(target)
                }
            }
        }
    }

    override fun quit(): Boolean {
        hasQuit = true
        return super.quit()
    }

    fun queueThumbnail(target: T, url: String) {
        Log.i(TAG, "Got a Url: $url")
        requestMap[target] = url
        requestHandler.obtainMessage(MESSAGE_DOWNLOAD, target).sendToTarget()
    }

    private fun handleRequest(target: T) {
        val url = requestMap[target] ?: return
        val bitmap = flickerFetchr.fetchPhoto(url) ?: return

        responseHandler.post(Runnable {
            if (requestMap[target] != url || hasQuit) requestMap.remove(target)
            onThumbnailDownloaded(target, bitmap)
        })
    }

//    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
//    fun setup() {
//        Log.i(TAG, "Starting background thread")
//    }
//
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
}
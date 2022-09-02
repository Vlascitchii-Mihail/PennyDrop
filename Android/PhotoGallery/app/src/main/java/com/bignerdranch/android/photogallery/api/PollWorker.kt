package com.bignerdranch.android.photogallery.api

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_MUTABLE
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.work.WorkerParameters
import androidx.work.Worker
import android.util.Log
import androidx.annotation.RequiresApi
import com.bignerdranch.android.photogallery.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.app.Notification

private const val TAG = "PollWorker"

//PollWorker checks Flickr for new photos
class PollWorker(private val context: Context, workerParams: WorkerParameters):  Worker(context, workerParams){
    @RequiresApi(Build.VERSION_CODES.S)

    //called from the background thread
    override fun doWork(): Result {
//        Log.i(TAG, "Work request triggered")
        val query = QueryPreferences.getStoredQuery(context)
        val lastResultId = QueryPreferences.getLastResultId(context)

        val items: List<GalleryItem> = if (query.isEmpty()) {
            FlickrFetchr().fetchPhotosRequest().execute().body()?.galleryItems
        } else {
            FlickrFetchr().searchPhotoRequest(query).execute().body()?.galleryItems
        } ?: emptyList()

        if (items.isEmpty()) {
            return Result.success()
        }

        val resultId = items.first().id
        if (resultId == lastResultId) {
            Log.i(TAG, "Got an old result: $resultId")
        } else {
            Log.i(TAG, "Got a new result: $resultId")
            QueryPreferences.setLastResultId(context, resultId)

            val intent = PhotoGalleryActivity.newIntent(context)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, FLAG_MUTABLE)
            val resources = context.resources
            val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setTicker(resources.getString(R.string.new_pictures_title))
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle(resources.getString(R.string.new_pictures_title))
                .setContentText(resources.getString(R.string.new_pictures_text))
                .setContentIntent(pendingIntent).setAutoCancel(true).build()

//            val notificationManager = NotificationManagerCompat.from(context)
//            notificationManager.notify(0, notification)
//            context.sendBroadcast(Intent(ACTION_SHOW_NOTIFICATION), PERM_PRIVATE)

            showBackgroundNotification(0, notification)
        }

        //returning a information about the success result
        return Result.success()
    }

    companion object {
        const val ACTION_SHOW_NOTIFICATION = "com.bignerdranch.android.photogallery.SHOW_NOTIFICATION"
        const val PERM_PRIVATE = "com.bignerdranch.android.photogallery.PRIVATE"
        const val REQUEST_CODE = "REQUEST_CODE"
        const val NOTIFICATION = "NOTIFICATION"
    }

    private fun showBackgroundNotification(requestCode: Int, notification: Notification) {
        val intent = Intent(ACTION_SHOW_NOTIFICATION).apply {
            putExtra(REQUEST_CODE, requestCode)
            putExtra(NOTIFICATION, notification)
        }
        context.sendOrderedBroadcast(intent, PERM_PRIVATE)
    }
}
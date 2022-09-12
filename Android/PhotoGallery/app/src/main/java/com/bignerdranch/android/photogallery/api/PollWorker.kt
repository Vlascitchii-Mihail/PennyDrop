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

    //runs in the background thread
    override fun doWork(): Result {
//        Log.i(TAG, "Work request triggered")

        //receiving the search query
        val query = QueryPreferences.getStoredQuery(context)

        //receiving the last photo's ID
        val lastResultId = QueryPreferences.getLastResultId(context)

        val items: List<GalleryItem> = if (query.isEmpty()) {

            //execute() - Synchronously send the request and return its response.
            //body() - The deserialized response body of a successful response.
            FlickrFetchr().fetchPhotosRequest().execute().body()?.galleryItems
        } else {

            //execute() - Synchronously send the request and return its response.
            //body() - The deserialized response body of a successful response.
            FlickrFetchr().searchPhotoRequest(query).execute().body()?.galleryItems

            //if isn't found any photo, returning the empty list
        } ?: emptyList()

        if (items.isEmpty()) {
            return Result.success()
        }

        val resultId = items.first().id
        if (resultId == lastResultId) {
            Log.i(TAG, "Got an old result: $resultId")
        } else {
            Log.i(TAG, "Got a new result: $resultId")

            //setting the last photo's ID
            QueryPreferences.setLastResultId(context, resultId)

            //creating a new intent for calling the app from the notification in the status bar
            //intent from main activity
            val intent = PhotoGalleryActivity.newIntent(context)

            //places the intent in the notification in the status bar
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, FLAG_MUTABLE)

            //resources to coll String constants from Resources
            val resources = context.resources

            //crating a new notification
            /**
             * @param NOTIFICATION_CHANNEL_ID - const val NOTIFICATION_CHANNEL_ID = "flickr_poll" from PhotoGalleryApplication
             */
            val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)

                //invisible description
                .setTicker(resources.getString(R.string.new_pictures_title))

                //icon of the notification view in the status bar
                .setSmallIcon(android.R.drawable.ic_menu_report_image)

                //setting title for the notification
                .setContentTitle(resources.getString(R.string.new_pictures_title))

                //setting text in the notification
                .setContentText(resources.getString(R.string.new_pictures_text))

                //notification click listener
                //setAutoCancel(true) - deleting the notification after click
                .setContentIntent(pendingIntent).setAutoCancel(true).build()

            //from(context) - NotificationManager from current context
//            val notificationManager = NotificationManagerCompat.from(context)

            //placing the notification
//            notificationManager.notify(0, notification)
//            context.sendBroadcast(Intent(ACTION_SHOW_NOTIFICATION), PERM_PRIVATE)

            //showing the notification
            /**
             * @param 0 - notification's ID
             */
            showBackgroundNotification(0, notification)
        }

        //returning a information about the success result
        return Result.success()
    }

    companion object {
        //intent filter
        const val ACTION_SHOW_NOTIFICATION = "com.bignerdranch.android.photogallery.SHOW_NOTIFICATION"

//        "com.bignerdranch.android.photogallery.PRIVATE" - permission's ID-->
        const val PERM_PRIVATE = "com.bignerdranch.android.photogallery.PRIVATE"
        const val REQUEST_CODE = "REQUEST_CODE"
        const val NOTIFICATION = "NOTIFICATION"
    }

    //creating and sending the broadcast intent by the ordered way
    private fun showBackgroundNotification(requestCode: Int, notification: Notification) {
        val intent = Intent(ACTION_SHOW_NOTIFICATION).apply {
            putExtra(REQUEST_CODE, requestCode)
            putExtra(NOTIFICATION, notification)
        }

        //sending the broadcast intent to the dynamic broadcast receiver by the ordered way
        context.sendOrderedBroadcast(intent, PERM_PRIVATE)
    }
}
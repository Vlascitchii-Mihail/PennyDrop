package com.bignerdranch.android.photogallery

import android.app.Application
import android.app.NotificationManager
import android.os.Build
import android.app.NotificationChannel

const val NOTIFICATION_CHANNEL_ID = "flickr_poll"

//creating app's notification settings
class PhotoGalleryApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            //name of the channel
            val name = getString(R.string.notification_channel_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            //creating a new channel
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance)
            val notificationManager = getSystemService(NotificationManager::class.java)

            //creating a new Notification Channel
            notificationManager.createNotificationChannel(channel)
        }
    }
}
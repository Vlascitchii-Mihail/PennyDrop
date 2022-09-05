package com.bignerdranch.android.photogallery

import android.app.Activity
import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.bignerdranch.android.photogallery.api.PollWorker

private const val TAG = "NotificationReceiver"

//registration a autonomous broadcast receiver in AndroidManifest
//but here we write his functions
class NotificationReceiver: BroadcastReceiver() {

    //calls when receiver gets the intent
    override fun onReceive(context: Context, intent: Intent) {
        Log.i(TAG, "received result: $resultCode")

        //if resultCode from VisibleFragment.onReceive
        if (resultCode != Activity.RESULT_OK) {
        //UI is active and receiving of translation ic canceled
            return
        }

        val requestCode = intent.getIntExtra(PollWorker.REQUEST_CODE, 0)
        val notification: Notification? = intent.getParcelableExtra(PollWorker.NOTIFICATION)
        val notificationManager = NotificationManagerCompat.from(context)

        //sending a new notification
        notificationManager.notify(requestCode, checkNotNull(notification))
    }
}
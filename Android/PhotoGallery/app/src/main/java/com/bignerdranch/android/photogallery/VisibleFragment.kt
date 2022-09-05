package com.bignerdranch.android.photogallery

import android.app.Activity
import androidx.fragment.app.Fragment
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import android.content.IntentFilter
import com.bignerdranch.android.photogallery.api.PollWorker
import android.util.Log

private const val TAG = "VisibleFragment"

//hides the notifications if app is running
open class VisibleFragment: Fragment() {

    //creating a new dynamic broadcast receiver
    private val onShowNotification = object: BroadcastReceiver() {

        //if app is running, this function will receive broadcast
        override fun onReceive(context: Context, intent: Intent) {
//            Toast.makeText(requireContext(), "Got a broadcast: ${intent.action}", Toast.LENGTH_SHORT).show()
            Log.i(TAG, "Canceling notification")

            //cancelling the broadcast
            resultCode = Activity.RESULT_CANCELED
        }
    }

    override fun onStart() {
        super.onStart()

        //creating a new intent filter
        val filter = IntentFilter(PollWorker.ACTION_SHOW_NOTIFICATION)

        //registering a new dynamic broadcast receiver
        requireActivity().registerReceiver(
            onShowNotification, filter, PollWorker.PERM_PRIVATE, null
        )
    }

    override fun onStop() {
        super.onStop()

        //unregistering the dynamic broadcast receiver
        requireActivity().unregisterReceiver(onShowNotification)
    }
}
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

open class VisibleFragment: Fragment() {
    private val onShowNotification = object: BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
//            Toast.makeText(requireContext(), "Got a broadcast: ${intent.action}", Toast.LENGTH_SHORT).show()
            Log.i(TAG, "Canceling notification")
            resultCode = Activity.RESULT_CANCELED
        }
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter(PollWorker.ACTION_SHOW_NOTIFICATION)
        requireActivity().registerReceiver(
            onShowNotification, filter, PollWorker.PERM_PRIVATE, null
        )
    }

    override fun onStop() {
        super.onStop()
        requireActivity().unregisterReceiver(onShowNotification)
    }
}
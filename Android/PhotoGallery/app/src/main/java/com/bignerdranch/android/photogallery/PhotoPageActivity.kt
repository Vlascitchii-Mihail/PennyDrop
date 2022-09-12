package com.bignerdranch.android.photogallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.net.Uri
import android.content.Intent
import androidx.fragment.app.Fragment

class PhotoPageActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_page)

        val fm = supportFragmentManager
        val currentFragment = fm.findFragmentById(R.id.fragment_container)

        if (currentFragment == null) {
//            val fragment = PhotoPageFragment.newInstance(intent.data)
            val fragment = intent.data?.let { PhotoPageFragment.newInstance(it) }
            fm.beginTransaction().add(R.id.fragment_container, fragment as Fragment).commit()
        }
    }

    companion object {
        fun newIntent(context: Context, photoPageUri: Uri): Intent {
            return Intent(context, PhotoPageActivity::class.java).apply {
                data = photoPageUri
            }
        }
    }
}
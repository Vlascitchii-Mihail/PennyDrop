package com.bignerdranch.android.photogallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.content.Intent

class PhotoGalleryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_gallery)

        //if fragment  is not in the container
        //or we can use supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        val isFragmentContainerEmpty = savedInstanceState == null
        if (isFragmentContainerEmpty) {

            //creating new Fragment's exemplar and adding it in the container
            //supportFragmentManager - Верните FragmentManager для взаимодействия с фрагментами, связанными с этим действием.
            //beginTransaction() - creates and return an exemplar of FragmentTransaction
            //add() - filling the exemplar of FragmentTransaction
            /**
             *@param R.id.fragment_container - container ID
             * @param PhotoGalleryFragment.newInstance() - fragment exemplar
             */
            //starts fragment functions onCreate(), onCreateView(), onViewCreate(), 0nAttach()
            //commit() - Планирует фиксацию этой транзакции.
            supportFragmentManager.beginTransaction().add(R.id.fragmentContainer, PhotoGalleryFragment.newInstance()).commit()
        }
    }

    companion object {

        //Intent is using for start the activity from the status bar
        fun newIntent(context: Context): Intent {
            return Intent(context, PhotoGalleryActivity::class.java)
        }
    }
}
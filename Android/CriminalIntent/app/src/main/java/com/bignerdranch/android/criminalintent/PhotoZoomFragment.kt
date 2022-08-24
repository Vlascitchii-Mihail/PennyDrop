package com.bignerdranch.android.criminalintent

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import java.io.File
import java.io.Serializable
import java.io.StringReader

private const val PHOTO = "com.bignerdranch.android.criminalintent.PhotoZoomFragment"

//zooming the photo
class PhotoZoomFragment: DialogFragment() {

    //returns dialog with photo
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        //creating AlertDialog
        val builder = AlertDialog.Builder(activity)

        //getting the layout inflater
        val inflater = requireActivity().layoutInflater

        //inflation the zoom_photo_crime
        val view = inflater.inflate(R.layout.zoom_photo_crime, null)

        //sets view in dialog
        builder.setView(view)

        //get reference CrimePicture in PhotoZoomFragment
        val zoom = view.findViewById(R.id.zoom_photo) as ImageView

        //get the image path argument
        val photoFileName = arguments?.getSerializable(PHOTO) as File

        //get the scale image
        val bitmap = getScaleBitmap(photoFileName.path, requireActivity())

        //setting the picture in the PhotoZoomFragment view
        zoom.setImageBitmap(bitmap)

        //creating the PhotoZoomFragment object and returning it
        return builder.create()
    }


    companion object {

        //transfer data from CrimeFragment to PhotoZoomFragment using the fragment's arguments
        fun newInstance(photoFileName: File): PhotoZoomFragment {
            val args = Bundle().apply {
                putSerializable(PHOTO, photoFileName)
            }

            //setting the data to PhotoZoomFragment arguments
            return PhotoZoomFragment().apply { arguments = args }
        }
    }
}
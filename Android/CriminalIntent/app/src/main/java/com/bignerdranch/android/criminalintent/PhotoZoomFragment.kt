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
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import java.io.File
import java.io.Serializable
import java.io.StringReader

class PhotoZoomFragment: DialogFragment() {

//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        super.onCreateDialog(savedInstanceState)
//        val view = inflater.inflate(R.layout.zoom_photo_crime, container, false)
//        val zoom = view.findViewById(R.id.zoom_photo) as ImageView
//
//
//        val photoFileName = arguments?.getSerializable("PHOTO") as String
//
//        zoom.setImageBitmap(BitmapFactory.decodeFile(requireContext().filesDir.path + "/" + photoFileName))
////        zoom.setImageURI(arguments?.getSerializable("PHOTO") as Uri)
//
//
//        return view
//    }
//
//    companion object {
//        fun newInstance(photoFileName: String): PhotoZoomFragment {
//            val args = Bundle().apply {
//                putSerializable("PHOTO", photoFileName)
//            }
//            return PhotoZoomFragment().apply { arguments = args }
//        }
//    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        val builder = AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.zoom_photo_crime, null)
        builder.setView(view)
        val zoom = view.findViewById(R.id.zoom_photo) as ImageView
        val photoFileName = arguments?.getSerializable("PHOTO") as File
        val bitmap = getScaleBitmap(photoFileName.path, requireActivity())
        zoom.setImageBitmap(bitmap)
        return builder.create()
    }


    companion object {
        fun newInstance(photoFileName: File): PhotoZoomFragment {
            val args = Bundle().apply {
                putSerializable("PHOTO", photoFileName)
            }
            return PhotoZoomFragment().apply { arguments = args }
        }
    }
}
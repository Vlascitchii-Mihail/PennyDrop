package com.bignerdranch.android.criminalintent

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.app.Activity
import android.graphics.Point
import android.hardware.display.DisplayManager
import kotlin.math.roundToInt

fun getScaleBitmap(path: String, destWidth: Int, destHeight: Int): Bitmap {
    //Reading size of image on SSD
    var options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeFile(path, options)

    val srcWidth = options.outWidth.toFloat()
    val srcHeight = options.outHeight.toFloat()

    //Find out how much need to decrease
    var inSampleSize = 1

    if (srcHeight > destHeight || srcWidth > destWidth) {
        val heightScale = srcHeight / destHeight
        val widthScale = srcWidth / destWidth

        val sampleScale = if (heightScale > widthScale) heightScale
        else widthScale

        inSampleSize = sampleScale.roundToInt()
    }

    options = BitmapFactory.Options()
    options.inSampleSize = inSampleSize

    //Reading and creating a final bitmap
    return BitmapFactory.decodeFile(path, options)
}

fun getScaleBitmap(path: String, activity: Activity): Bitmap {
    val size = Point()
//    activity.windowManager.defaultDisplay.getSize(size)
//    return getScaleBitmap(path, size.x, size.y)
    val dimensions = activity.windowManager.currentWindowMetrics.bounds
    return getScaleBitmap(path, dimensions.width(), dimensions.height())
}
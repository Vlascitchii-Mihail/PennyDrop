package com.bignerdranch.android.criminalintent

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.app.Activity
import android.graphics.Point
import android.hardware.display.DisplayManager
import kotlin.math.roundToInt

//scaling a photo
fun getScaleBitmap(path: String, destWidth: Int, destHeight: Int): Bitmap {

    //Reading size of image on SSD
    var options = BitmapFactory.Options()

    //Если включить (true) этот параметр, то система не будет создавать Bitmap,
    // а только вернет информацию о изображение в следующих полях:
    //outWidth – ширина
    //outHeight – высота
    //outMimeType – mimetype
    options.inJustDecodeBounds = true

    //reading the image parameters from file
    BitmapFactory.decodeFile(path, options)

    //getting the source parameters
    val srcWidth = options.outWidth.toFloat()
    val srcHeight = options.outHeight.toFloat()

    //Find out how much need to decrease a photo
    //if 1 => 1 to 1
    //if 2 => 1 to 4
    var inSampleSize = 1

    if (srcHeight > destHeight || srcWidth > destWidth) {
        val heightScale = srcHeight / destHeight
        val widthScale = srcWidth / destWidth

        val sampleScale = if (heightScale > widthScale) heightScale
        else widthScale

        inSampleSize = sampleScale.roundToInt()
    }

    options = BitmapFactory.Options()

    //Позволяет указать коэффициент уменьшения размера изображения при чтении.
    // Он должен быть кратным 2. Если зададите другое число, то оно будет
    // изменено на ближайшее число меньшее вашего и кратное 2
    options.inSampleSize = inSampleSize

    //Reading and creating a final bitmap
    return BitmapFactory.decodeFile(path, options)
}

//scaling Bitmap under the size of the activity
fun getScaleBitmap(path: String, activity: Activity): Bitmap {
    val size = Point()
//    activity.windowManager.defaultDisplay.getSize(size)
//    return getScaleBitmap(path, size.x, size.y)

    //returns size of the display
    val dimensions = activity.windowManager.currentWindowMetrics.bounds

    //downscaling the photo's size to the display's size
    return getScaleBitmap(path, dimensions.width(), dimensions.height())
}
package com.bignerdranch.android.photogallery

import com.google.gson.annotations.SerializedName
import android.net.Uri

//class which describe photo's object
// @SerializedName("url_s") - association url_s from Rest API with GalleryItem.uel
data class GalleryItem (var title: String = "", var id: String = "",
                        @SerializedName("url_s") var url: String = "",
                        @SerializedName("owner") var owner: String = "") {
    val photoPageUri: Uri get() {
        return Uri.parse("https://www.flickr.com/photos/")
            .buildUpon().appendPath(owner).appendPath(id).build()
    }
}
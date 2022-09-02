package com.bignerdranch.android.photogallery.api

import androidx.paging.PagingData
import com.bignerdranch.android.photogallery.GalleryItem
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.flow.Flow
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.bignerdranch.android.photogallery.PagingPhotoSource

class PhotoResponse {

    // @SerializedName("url_s") - association url_s from Rest API with GalleryItem.uel
    @SerializedName("photo") lateinit var galleryItems: List<GalleryItem>
    private var page: Int? = null
    private var pages: Int? = null

    fun setPage(page: Int) {
        this.page = page
    }

    fun setPages(pages: Int) {
        this.pages = pages
    }

    fun getPage(): Int? {
        return page
    }

    fun getPages(): Int? {
        return pages
    }
}
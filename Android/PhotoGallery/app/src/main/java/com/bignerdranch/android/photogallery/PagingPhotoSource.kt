package com.bignerdranch.android.photogallery

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bignerdranch.android.photogallery.api.PhotoResponse
import java.lang.Exception

typealias UserPageLoader = suspend (pageIndex: Int, pageSize: Int) -> List<GalleryItem>

class PagingPhotoSource(
    private val loader: PhotoResponse
): PagingSource<Int, GalleryItem>() {
    override fun getRefreshKey(state: PagingState<Int, GalleryItem>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GalleryItem> {
        val page = params.key ?: 1
//        val pageSize = params.loadSize

        return try {
            val photos = loader.galleryItems

            return LoadResult.Page(
                data = photos,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (photos.size == params.loadSize) page + 1 else null //(params.loadSize / pageSize)
            )
        } catch(e: Exception) {
            LoadResult.Error(throwable = e)
        }
    }
}
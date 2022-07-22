package com.example.android.codelabs.paging.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlin.math.max
import java.time.LocalDateTime

private const val STARTING_KEY = 0
private val firstArticleCreatedTime = LocalDateTime.now()


class ArticlePagingSource: PagingSource<Int, Article>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        TODO()
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        TODO()
    }

    private fun ensureValidKey(key: Int) = max(STARTING_KEY, key)
}
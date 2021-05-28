package me.ajay.imagegallery.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import me.ajay.imagegallery.api.ImageSearchAPI
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageSearchRepository @Inject constructor(private val imageSearchAPI: ImageSearchAPI) {

    fun getSearchResult(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 8,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ImagePagingSource(imageSearchAPI, query) }
        ).flow
}
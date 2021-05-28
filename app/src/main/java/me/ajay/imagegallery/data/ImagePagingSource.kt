package me.ajay.imagegallery.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import me.ajay.imagegallery.api.ImageSearchAPI
import retrofit2.HttpException
import java.io.IOException

private const val START_INDEX = 1

class ImagePagingSource(
    private val searchImageAPI: ImageSearchAPI,
    private val query: String
) : PagingSource<Int, GalleryImage>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GalleryImage> {
        val position = params.key ?: START_INDEX
        return try {
            val response = searchImageAPI.callSearchAPI(
                ImageSearchAPI.ACCESS_KEY, query,
                ImageSearchAPI.DEFAULT_IMAGE_TYPE, position, params.loadSize
            )
            val images = response.hits

            LoadResult.Page(
                data = images,
                prevKey = if (position == START_INDEX) null else position - 1,
                nextKey = if (images.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            Log.e(PagingSource::class.simpleName, exception.message.toString() )
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            Log.e(PagingSource::class.simpleName, exception.message.toString() )
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, GalleryImage>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}
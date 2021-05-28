package me.ajay.imagegallery.ui.gallery

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flatMapLatest
import me.ajay.imagegallery.data.ImageSearchRepository
import javax.inject.Inject

const val EMPTY_QUERY: String = ""
const val SEARCHED_QUERY: String = "searched_query"

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val repository: ImageSearchRepository,
    state: SavedStateHandle
) : ViewModel() {
    private val searchQuery = state.getLiveData(SEARCHED_QUERY, EMPTY_QUERY)
    private val imagesFlow = searchQuery.asFlow().flatMapLatest { query ->
        repository.getSearchResult(query).cachedIn(viewModelScope)
    }
    val images = imagesFlow.asLiveData()
    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }
}
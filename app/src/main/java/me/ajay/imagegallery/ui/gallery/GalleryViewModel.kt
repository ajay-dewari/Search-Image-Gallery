package me.ajay.imagegallery.ui.gallery

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import me.ajay.imagegallery.data.GalleryImage
import me.ajay.imagegallery.data.ImageSearchRepository
import javax.inject.Inject

const val EMPTY_QUERY: String = ""
const val SEARCHED_QUERY: String = "searched_query"

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val repository: ImageSearchRepository,
    state: SavedStateHandle
) : ViewModel() {
    private val eventChannel = Channel<GalleryEvent>()
    private val searchQuery = state.getLiveData(SEARCHED_QUERY, EMPTY_QUERY)
    private val imagesFlow = searchQuery.asFlow().flatMapLatest { query ->
        repository.getSearchResult(query).cachedIn(viewModelScope)
    }
    val galleryEvent = eventChannel.receiveAsFlow()
    val images = imagesFlow.asLiveData()
    val pendingQuery = MutableLiveData(EMPTY_QUERY);

    fun setSearchQuery(query: String) {
        searchQuery.value = query
        pendingQuery.value = EMPTY_QUERY
    }

    fun onRecyclerViewItemClicked(image: GalleryImage) {
        viewModelScope.launch {
            eventChannel.send(GalleryEvent.NavigateToDetails(image))
        }
    }

    fun onRetryClicked() {
        viewModelScope.launch {
            eventChannel.send(GalleryEvent.RetryLoading)
        }
    }

    sealed class GalleryEvent {
        data class NavigateToDetails(val image: GalleryImage) : GalleryEvent()
        object RetryLoading : GalleryEvent()
    }
}
package me.ajay.imagegallery.ui.gallery

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import me.ajay.imagegallery.data.ImageSearchRepository
import javax.inject.Inject

const val EMPTY_QUERY: String = ""

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val repository: ImageSearchRepository,
    state: SavedStateHandle
) : ViewModel() {
    private val searchQuery = MutableStateFlow(EMPTY_QUERY);

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }
}
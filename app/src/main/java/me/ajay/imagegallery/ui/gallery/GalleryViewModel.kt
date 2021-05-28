package me.ajay.imagegallery.ui.gallery

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

const val EMPTY_QUERY: String = ""

@HiltViewModel
class GalleryViewModel @Inject constructor(): ViewModel() {

    val searchQuery = MutableStateFlow(EMPTY_QUERY);


    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }
}
package me.ajay.imagegallery.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import me.ajay.imagegallery.data.GalleryImage
import javax.inject.Inject

@HiltViewModel
class DetailsFragmentViewModel @Inject constructor(state: SavedStateHandle) :
    ViewModel() {
    val imageItem = state.get<GalleryImage>("image")

    fun getHostName(url: String): String =
        url.substring(url.indexOf("//")+2, url.indexOf(".com"))
}
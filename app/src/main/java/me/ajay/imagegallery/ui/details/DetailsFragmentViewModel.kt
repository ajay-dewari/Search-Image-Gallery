package me.ajay.imagegallery.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import me.ajay.imagegallery.data.GalleryImage
import me.ajay.imagegallery.ui.gallery.GalleryViewModel
import java.net.URL
import javax.inject.Inject


@HiltViewModel
class DetailsFragmentViewModel @Inject constructor(state: SavedStateHandle) :
    ViewModel() {
    private val eventChannel = Channel<ImageDetailEvent>()
    val detailsEvent = eventChannel.receiveAsFlow()
    val imageItem = state.get<GalleryImage>("image")

    fun getHostName(url: String): String = URL(url).host

    fun onWebSourceClicked() {
        viewModelScope.launch {
            eventChannel.send(ImageDetailEvent.NavigateToWebSource)
        }
    }

    sealed class ImageDetailEvent {
        object NavigateToWebSource : ImageDetailEvent()
    }
}
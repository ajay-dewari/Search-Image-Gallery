package me.ajay.imagegallery.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GalleryImage(
    val id: String,
    val pageURL: String,
    val previewURL: String,
    val tags: String,
    val largeImageURL: String,
    val favorites: Long,
    val likes: Long,
    val views: Long,
    val user: String,
    val userImageURL: String,
) : Parcelable
package me.ajay.imagegallery.api

import me.ajay.imagegallery.data.GalleryImage


data class ImageSearchResponse(val hits : List<GalleryImage>) {
}
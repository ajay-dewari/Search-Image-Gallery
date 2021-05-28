package me.ajay.imagegallery.api

import retrofit2.http.GET
import retrofit2.http.Query

interface ImageSearchAPI {

    @GET("api")
    suspend fun callSearchAPI(
        @Query("key") key: String,
        @Query("q") query: String,
        @Query("image_type") imageType: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): ImageSearchResponse
}
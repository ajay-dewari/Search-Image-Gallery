package me.ajay.imagegallery.api

import retrofit2.http.GET
import retrofit2.http.Query

interface ImageSearchAPI {

    companion object {
        const val BASE_URL = "https://pixabay.com/"
        const val ACCESS_KEY = "21799998-e5032c294d238b7208ef0e3da"
        //Accepted values: "all", "photo", "illustration", "vector"
        const val DEFAULT_IMAGE_TYPE = "photo"
    }

    @GET("api")
    suspend fun callSearchAPI(
        @Query("key") key: String,
        @Query("q") query: String,
        @Query("image_type") imageType: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): ImageSearchResponse
}
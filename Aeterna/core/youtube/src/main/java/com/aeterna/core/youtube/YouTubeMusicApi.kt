package com.aeterna.core.youtube

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface YouTubeMusicApi {

    // Placeholder for search results - actual endpoint might be different
    @Headers("Content-Type: application/json")
    @POST("youtubei/v1/search")
    suspend fun search(
        @Header("Authorization") authorization: String,
        @Body body: RequestBody
    ): ResponseBody

    // Placeholder for fetching home feed content
    @Headers("Content-Type: application/json")
    @POST("youtubei/v1/browse")
    suspend fun getHomeFeed(
        @Header("Authorization") authorization: String,
        @Body body: RequestBody
    ): ResponseBody

    // Placeholder for fetching user library content (e.g., liked songs, playlists)
    @Headers("Content-Type: application/json")
    @POST("youtubei/v1/browse")
    suspend fun getLibrary(
        @Header("Authorization") authorization: String,
        @Body body: RequestBody
    ): ResponseBody

    // Placeholder for fetching playlist items
    @Headers("Content-Type: application/json")
    @POST("youtubei/v1/browse")
    suspend fun getPlaylistItems(
        @Header("Authorization") authorization: String,
        @Body body: RequestBody
    ): ResponseBody

    // Placeholder for fetching song details (e.g., for playback URL)
    @Headers("Content-Type: application/json")
    @POST("youtubei/v1/player")
    suspend fun getSongDetails(
        @Header("Authorization") authorization: String,
        @Body body: RequestBody
    ): ResponseBody
}
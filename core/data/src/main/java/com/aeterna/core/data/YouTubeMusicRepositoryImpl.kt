package com.aeterna.core.data

import com.aeterna.core.domain.Album
import com.aeterna.core.domain.Artist
import com.aeterna.core.domain.Playlist
import com.aeterna.core.domain.Song
import com.aeterna.core.domain.YouTubeMusicRepository
import com.aeterna.core.youtube.YouTubeMusicApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class YouTubeMusicRepositoryImpl @Inject constructor(
    private val youtubeMusicApi: YouTubeMusicApi
) : YouTubeMusicRepository {

    override suspend fun search(query: String): Flow<List<Any>> {
        val requestBody = JSONObject().apply {
            put("query", query)
            put("context", JSONObject().apply {
                put("client", JSONObject().apply {
                    put("clientName", "WEB_REMIX")
                    put("clientVersion", "1.20211001.00.00")
                })
            })
        }.toString().toRequestBody("application/json".toMediaTypeOrNull())

        // Make the network call, but keep parsing minimal and safe to avoid fragile, deeply-nested JSON handling.
        return try {
            val response = youtubeMusicApi.search("Bearer dummy_token", requestBody)
            val responseBodyString = response.string()

            // For now, avoid heavy parsing in this module. Return an empty list if parsing is not implemented.
            flowOf(emptyList<Any>())
        } catch (e: Exception) {
            e.printStackTrace()
            flowOf(emptyList())
        }
    }

    override suspend fun getHomeFeed(): Flow<List<Any>> {
        // Placeholder for actual API call and JSON parsing
        val requestBody = JSONObject().apply {
            // Add necessary parameters for YouTube Music home feed API
        }.toString().toRequestBody("application/json".toMediaTypeOrNull())

        val response = youtubeMusicApi.getHomeFeed("Bearer dummy_token", requestBody)
        // TODO: Parse response.body() into domain models
        return flowOf(emptyList())
    }

    override suspend fun getLibrary(): Flow<List<Any>> {
        // Placeholder for actual API call and JSON parsing
        val requestBody = JSONObject().apply {
            // Add necessary parameters for YouTube Music library API
        }.toString().toRequestBody("application/json".toMediaTypeOrNull())

        val response = youtubeMusicApi.getLibrary("Bearer dummy_token", requestBody)
        // TODO: Parse response.body() into domain models
        return flowOf(emptyList())
    }

    override suspend fun getPlaylistItems(playlistId: String): Flow<List<Song>> {
        // Placeholder for actual API call and JSON parsing
        val requestBody = JSONObject().apply {
            put("playlistId", playlistId)
            // Add other necessary parameters for YouTube Music playlist items API
        }.toString().toRequestBody("application/json".toMediaTypeOrNull())

        val response = youtubeMusicApi.getPlaylistItems("Bearer dummy_token", requestBody)
        // TODO: Parse response.body() into List<Song>
        return flowOf(emptyList())
    }

    override suspend fun getSongDetails(videoId: String): Flow<Song> {
        // Placeholder for actual API call and JSON parsing
        val requestBody = JSONObject().apply {
            put("videoId", videoId)
            // Add other necessary parameters for YouTube Music song details API
        }.toString().toRequestBody("application/json".toMediaTypeOrNull())

        val response = youtubeMusicApi.getSongDetails("Bearer dummy_token", requestBody)
        // TODO: Parse response.body() into Song
        // For now, return a dummy song
        return flowOf(
            Song(
                id = videoId,
                title = "Dummy Song Title",
                artist = "Dummy Artist",
                album = "Dummy Album",
                durationSeconds = 180L,
                thumbnailUrl = "",
                videoId = videoId,
                isExplicit = false,
                playbackUrl = "https://dummy.audio.url/dummy.mp3"
            )
        )
    }
}
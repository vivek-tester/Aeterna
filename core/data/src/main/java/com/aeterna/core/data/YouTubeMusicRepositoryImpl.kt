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

        val response = youtubeMusicApi.search("Bearer dummy_token", requestBody)
        val responseBodyString = response.body?.string()

        val results = mutableListOf<Any>()
        if (responseBodyString != null) {
            try {
                val jsonResponse = JSONObject(responseBodyString)
                // Simplified parsing: Look for common keys that might contain results
                val contents = jsonResponse.optJSONObject("contents")
                    ?.optJSONObject("tabbedSearchResultsRenderer")
                    ?.optJSONArray("tabs")
                    ?.optJSONObject(0) // Assuming first tab
                    ?.optJSONObject("tabRenderer")
                    ?.optJSONObject("content")
                    ?.optJSONObject("sectionListRenderer")
                    ?.optJSONArray("contents")

                contents?.let {
                    for (i in 0 until it.length()) {
                        val section = it.optJSONObject(i)
                        val itemSectionRenderer = section?.optJSONObject("itemSectionRenderer")
                        val itemContents = itemSectionRenderer?.optJSONArray("contents")

                        itemContents?.let { innerContents ->
                            for (j in 0 until innerContents.length()) {
                                val item = innerContents.optJSONObject(j)
                                // Attempt to parse as Song, Album, Artist, Playlist
                                item?.optJSONObject("musicResponsiveListItemRenderer")?.let { listItem ->
                                    val title = listItem.optJSONObject("flexColumns")
                                        ?.optJSONObject("musicResponsiveListItemFlexColumnRenderer")
                                        ?.optJSONObject("text")
                                        ?.optJSONArray("runs")
                                        ?.optJSONObject(0)
                                        ?.optString("text") ?: "Unknown Title"
                                    val artist = listItem.optJSONObject("flexColumns")
                                        ?.optJSONObject("musicResponsiveListItemFlexColumnRenderer")
                                        ?.optJSONObject("text")
                                        ?.optJSONArray("runs")
                                        ?.optJSONObject(2) // Assuming artist is at index 2
                                        ?.optString("text") ?: "Unknown Artist"
                                    val thumbnail = listItem.optJSONObject("thumbnail")
                                        ?.optJSONObject("musicThumbnailRenderer")
                                        ?.optJSONObject("thumbnail")
                                        ?.optJSONArray("thumbnails")
                                        ?.optJSONObject(0)
                                        ?.optString("url") ?: ""
                                    val videoId = listItem.optString("videoId") ?: ""
                                    val durationText = listItem.optJSONObject("flexColumns")
                                        ?.optJSONObject("musicResponsiveListItemFlexColumnRenderer")
                                        ?.optJSONObject("text")
                                        ?.optJSONArray("runs")
                                        ?.optJSONObject(4) // Assuming duration is at index 4
                                        ?.optString("text") ?: "0:00"
                                    val durationSeconds = durationText.split(":")
                                        .map { it.toLongOrNull() ?: 0 }
                                        .let { if (it.size == 2) it * 60 + it else 0 }

                                    results.add(
                                        Song(
                                            id = videoId,
                                            title = title,
                                            artist = artist,
                                            album = "Unknown Album", // Placeholder
                                            durationSeconds = durationSeconds,
                                            thumbnailUrl = thumbnail,
                                            videoId = videoId,
                                            isExplicit = false,
                                            playbackUrl = null // Will be fetched dynamically
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle JSON parsing error
            }
        }
        return flowOf(results)
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
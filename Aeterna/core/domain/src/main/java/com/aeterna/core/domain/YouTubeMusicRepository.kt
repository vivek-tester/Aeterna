package com.aeterna.core.domain

import kotlinx.coroutines.flow.Flow

interface YouTubeMusicRepository {
    suspend fun search(query: String): Flow<List<Any>> // Placeholder for search results
    suspend fun getHomeFeed(): Flow<List<Any>> // Placeholder for home feed content
    suspend fun getLibrary(): Flow<List<Any>> // Placeholder for user library content
    suspend fun getPlaylistItems(playlistId: String): Flow<List<Song>> // Placeholder for playlist items
    suspend fun getSongDetails(videoId: String): Flow<Song> // Placeholder for song details
}
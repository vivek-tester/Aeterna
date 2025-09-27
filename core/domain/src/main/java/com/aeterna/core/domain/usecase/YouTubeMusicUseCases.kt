package com.aeterna.core.domain.usecase

import com.aeterna.core.domain.Song
import com.aeterna.core.domain.YouTubeMusicRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchYouTubeMusic @Inject constructor(
    private val repository: YouTubeMusicRepository
) {
    suspend operator fun invoke(query: String): Flow<List<Any>> {
        return repository.search(query)
    }
}

class GetHomeFeed @Inject constructor(
    private val repository: YouTubeMusicRepository
) {
    suspend operator fun invoke(): Flow<List<Any>> {
        return repository.getHomeFeed()
    }
}

class GetLibraryContent @Inject constructor(
    private val repository: YouTubeMusicRepository
) {
    suspend operator fun invoke(): Flow<List<Any>> {
        return repository.getLibrary()
    }
}

class GetPlaylistItems @Inject constructor(
    private val repository: YouTubeMusicRepository
) {
    suspend operator fun invoke(playlistId: String): Flow<List<Song>> {
        return repository.getPlaylistItems(playlistId)
    }
}

class GetSongDetails @Inject constructor(
    private val repository: YouTubeMusicRepository
) {
    suspend operator fun invoke(videoId: String): Flow<Song> {
        return repository.getSongDetails(videoId)
    }
}

data class YouTubeMusicUseCases @Inject constructor(
    val searchYouTubeMusic: SearchYouTubeMusic,
    val getHomeFeed: GetHomeFeed,
    val getLibraryContent: GetLibraryContent,
    val getPlaylistItems: GetPlaylistItems,
    val getSongDetails: GetSongDetails
)
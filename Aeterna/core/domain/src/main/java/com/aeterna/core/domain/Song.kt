package com.aeterna.core.domain

data class Song(
    val id: String,
    val title: String,
    val artist: String,
    val album: String,
    val durationSeconds: Long, // Renamed for clarity
    val thumbnailUrl: String,
    val videoId: String, // YouTube video ID for playback
    val isExplicit: Boolean = false,
    val playbackUrl: String? = null // This will be fetched dynamically
)
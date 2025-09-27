package com.aeterna.core.domain

data class Playlist(
    val id: String,
    val title: String,
    val thumbnailUrl: String,
    val owner: String,
    val songCount: Int,
    val description: String? = null,
    val isEditable: Boolean = false,
    val isPublic: Boolean = false,
    val lastUpdated: Long? = null, // Timestamp
    val songs: List<Song>? = null // Optional list of songs in the playlist
)
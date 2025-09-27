package com.aeterna.core.domain

data class Artist(
    val id: String,
    val name: String,
    val thumbnailUrl: String,
    val description: String? = null,
    val followerCount: String? = null, // e.g., "1.2M"
    val albums: List<Album>? = null // Optional list of albums by the artist
)
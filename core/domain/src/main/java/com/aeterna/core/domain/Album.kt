package com.aeterna.core.domain

data class Album(
    val id: String,
    val title: String,
    val artist: String, // Main artist
    val thumbnailUrl: String,
    val year: Int,
    val trackCount: Int? = null,
    val releaseDate: String? = null,
    val description: String? = null,
    val durationSeconds: Long? = null,
    val songs: List<Song>? = null // Optional list of songs in the album
)
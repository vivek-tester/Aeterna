package com.aeterna.core.domain

import androidx.media3.common.MediaItem

interface PlayerRepository {
    fun playMedia(mediaItem: MediaItem)
    fun pauseMedia()
    fun resumeMedia()
    fun seekTo(positionMs: Long)
    fun getCurrentPosition(): Long
    fun getDuration(): Long
    fun isPlaying(): Boolean
}
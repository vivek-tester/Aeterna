package com.aeterna.aeterna.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private lateinit var exoPlayer: ExoPlayer
    private lateinit var mediaSession: MediaSession

    fun initializePlayer() {
        exoPlayer = ExoPlayer.Builder(context).build()
        mediaSession = MediaSession.Builder(context, exoPlayer)
            .setId("AeternaMediaSession")
            .build()
    }

    fun releasePlayer() {
        mediaSession.release()
        exoPlayer.release()
    }

    fun playMedia(mediaItem: MediaItem) {
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
    }

    fun pauseMedia() {
        exoPlayer.playWhenReady = false
    }

    fun resumeMedia() {
        exoPlayer.playWhenReady = true
    }

    fun seekTo(positionMs: Long) {
        exoPlayer.seekTo(positionMs)
    }

    fun getCurrentPosition(): Long {
        return exoPlayer.currentPosition
    }

    fun getDuration(): Long {
        return exoPlayer.duration
    }

    fun isPlaying(): Boolean {
        return exoPlayer.isPlaying
    }

    fun getMediaSession(): MediaSession = mediaSession
}
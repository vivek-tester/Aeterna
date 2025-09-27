package com.aeterna.aeterna.player

import androidx.media3.common.MediaItem
import com.aeterna.core.domain.PlayerRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerRepositoryImpl @Inject constructor(
    private val playerManager: PlayerManager
) : PlayerRepository {
    override fun playMedia(mediaItem: MediaItem) {
        playerManager.playMedia(mediaItem)
    }

    override fun pauseMedia() {
        playerManager.pauseMedia()
    }

    override fun resumeMedia() {
        playerManager.resumeMedia()
    }

    override fun seekTo(positionMs: Long) {
        playerManager.seekTo(positionMs)
    }

    override fun getCurrentPosition(): Long {
        return playerManager.getCurrentPosition()
    }

    override fun getDuration(): Long {
        return playerManager.getDuration()
    }

    override fun isPlaying(): Boolean {
        return playerManager.isPlaying()
    }
}
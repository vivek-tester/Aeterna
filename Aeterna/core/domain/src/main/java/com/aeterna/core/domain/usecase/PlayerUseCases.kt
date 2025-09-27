package com.aeterna.core.domain.usecase

import androidx.media3.common.MediaItem
import com.aeterna.core.domain.PlayerRepository
import javax.inject.Inject

class PlayMedia @Inject constructor(
    private val repository: PlayerRepository
) {
    operator fun invoke(mediaItem: MediaItem) {
        repository.playMedia(mediaItem)
    }
}

class PauseMedia @Inject constructor(
    private val repository: PlayerRepository
) {
    operator fun invoke() {
        repository.pauseMedia()
    }
}

class ResumeMedia @Inject constructor(
    private val repository: PlayerRepository
) {
    operator fun invoke() {
        repository.resumeMedia()
    }
}

class SeekTo @Inject constructor(
    private val repository: PlayerRepository
) {
    operator fun invoke(positionMs: Long) {
        repository.seekTo(positionMs)
    }
}

class GetCurrentPosition @Inject constructor(
    private val repository: PlayerRepository
) {
    operator fun invoke(): Long {
        return repository.getCurrentPosition()
    }
}

class GetDuration @Inject constructor(
    private val repository: PlayerRepository
) {
    operator fun invoke(): Long {
        return repository.getDuration()
    }
}

class IsPlaying @Inject constructor(
    private val repository: PlayerRepository
) {
    operator fun invoke(): Boolean {
        return repository.isPlaying()
    }
}

data class PlayerUseCases @Inject constructor(
    val playMedia: PlayMedia,
    val pauseMedia: PauseMedia,
    val resumeMedia: ResumeMedia,
    val seekTo: SeekTo,
    val getCurrentPosition: GetCurrentPosition,
    val getDuration: GetDuration,
    val isPlaying: IsPlaying
)
package com.aeterna.aeterna.di

import com.aeterna.core.domain.usecase.AuthUseCases
import com.aeterna.core.domain.usecase.ClearAuthState
import com.aeterna.core.domain.usecase.ExchangeToken
import com.aeterna.core.domain.usecase.GetAuthRequestIntent
import com.aeterna.core.domain.usecase.GetAuthState
import com.aeterna.core.domain.usecase.PlayerUseCases
import com.aeterna.core.domain.usecase.SaveAuthState
import com.aeterna.core.domain.usecase.YouTubeMusicUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideAuthUseCases(
            getAuthRequestIntent: GetAuthRequestIntent,
            exchangeToken: ExchangeToken,
            getAuthState: GetAuthState,
            saveAuthState: SaveAuthState,
            clearAuthState: ClearAuthState
    ): AuthUseCases {
        return AuthUseCases(
                getAuthRequestIntent = getAuthRequestIntent,
                exchangeToken = exchangeToken,
                getAuthState = getAuthState,
                saveAuthState = saveAuthState,
                clearAuthState = clearAuthState
        )
    }

    @Provides
    @Singleton
    fun providePlayerUseCases(
            playMedia: com.aeterna.core.domain.usecase.PlayMedia,
            pauseMedia: com.aeterna.core.domain.usecase.PauseMedia,
            resumeMedia: com.aeterna.core.domain.usecase.ResumeMedia,
            seekTo: com.aeterna.core.domain.usecase.SeekTo,
            getCurrentPosition: com.aeterna.core.domain.usecase.GetCurrentPosition,
            getDuration: com.aeterna.core.domain.usecase.GetDuration,
            isPlaying: com.aeterna.core.domain.usecase.IsPlaying
    ): PlayerUseCases {
        return PlayerUseCases(
                playMedia = playMedia,
                pauseMedia = pauseMedia,
                resumeMedia = resumeMedia,
                seekTo = seekTo,
                getCurrentPosition = getCurrentPosition,
                getDuration = getDuration,
                isPlaying = isPlaying
        )
    }

    @Provides
    @Singleton
    fun provideYouTubeMusicUseCases(
            searchYouTubeMusic: com.aeterna.core.domain.usecase.SearchYouTubeMusic,
            getHomeFeed: com.aeterna.core.domain.usecase.GetHomeFeed,
            getLibraryContent: com.aeterna.core.domain.usecase.GetLibraryContent,
            getPlaylistItems: com.aeterna.core.domain.usecase.GetPlaylistItems,
            getSongDetails: com.aeterna.core.domain.usecase.GetSongDetails
    ): YouTubeMusicUseCases {
        return YouTubeMusicUseCases(
                searchYouTubeMusic = searchYouTubeMusic,
                getHomeFeed = getHomeFeed,
                getLibraryContent = getLibraryContent,
                getPlaylistItems = getPlaylistItems,
                getSongDetails = getSongDetails
        )
    }
}

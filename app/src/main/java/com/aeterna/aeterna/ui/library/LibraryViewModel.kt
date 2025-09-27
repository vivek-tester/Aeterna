package com.aeterna.aeterna.ui.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aeterna.core.domain.Playlist
import com.aeterna.core.domain.Song
import com.aeterna.core.domain.usecase.YouTubeMusicUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

data class LibraryState(
        val isLoading: Boolean = false,
        val playlists: List<Playlist> = emptyList(),
        val likedSongs: List<Song> = emptyList(),
        val downloadedSongs: List<Song> = emptyList(),
        val error: String? = null
)

@HiltViewModel
class LibraryViewModel @Inject constructor(private val youtubeMusicUseCases: YouTubeMusicUseCases) :
        ViewModel() {

    private val _libraryState = MutableStateFlow(LibraryState())
    val libraryState: StateFlow<LibraryState> = _libraryState.asStateFlow()

    init {
        loadLibrary()
    }

    private fun loadLibrary() {
        viewModelScope.launch {
            _libraryState.value = _libraryState.value.copy(isLoading = true)

            try {
                youtubeMusicUseCases
                        .getLibraryContent()
                        .catch { error ->
                            _libraryState.value =
                                    _libraryState.value.copy(
                                            isLoading = false,
                                            error = error.message
                                    )
                        }
                        .collect { libraryContent ->
                            // Parse library content into categories
                            val playlists = libraryContent.filterIsInstance<Playlist>()
                            // For now, mock some data
                            _libraryState.value =
                                    _libraryState.value.copy(
                                            isLoading = false,
                                            playlists = getMockPlaylists(),
                                            likedSongs = getMockLikedSongs(),
                                            downloadedSongs = emptyList(),
                                            error = null
                                    )
                        }
            } catch (e: Exception) {
                _libraryState.value = _libraryState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    private fun getMockPlaylists(): List<Playlist> {
        return listOf(
                Playlist(
                        id = "1",
                        title = "My Favorites",
                        thumbnailUrl = "",
                        owner = "You",
                        songCount = 25,
                        isEditable = true
                ),
                Playlist(
                        id = "2",
                        title = "Chill Vibes",
                        thumbnailUrl = "",
                        owner = "You",
                        songCount = 18,
                        isEditable = true
                ),
                Playlist(
                        id = "3",
                        title = "Workout Mix",
                        thumbnailUrl = "",
                        owner = "You",
                        songCount = 32,
                        isEditable = true
                )
        )
    }

    private fun getMockLikedSongs(): List<Song> {
        return listOf(
                Song(
                        id = "1",
                        title = "Blinding Lights",
                        artist = "The Weeknd",
                        album = "After Hours",
                        durationSeconds = 200,
                        thumbnailUrl = "",
                        videoId = "4NRXx6U8ABQ"
                ),
                Song(
                        id = "2",
                        title = "Watermelon Sugar",
                        artist = "Harry Styles",
                        album = "Fine Line",
                        durationSeconds = 174,
                        thumbnailUrl = "",
                        videoId = "E07s5ZYygMg"
                ),
                Song(
                        id = "3",
                        title = "Good 4 U",
                        artist = "Olivia Rodrigo",
                        album = "SOUR",
                        durationSeconds = 178,
                        thumbnailUrl = "",
                        videoId = "gNi_6U5Pm_o"
                )
        )
    }

    fun refreshLibrary() {
        loadLibrary()
    }

    fun createPlaylist(name: String) {
        viewModelScope.launch {
            // TODO: Implement playlist creation
        }
    }

    fun deletePlaylist(playlistId: String) {
        viewModelScope.launch {
            // TODO: Implement playlist deletion
        }
    }
}

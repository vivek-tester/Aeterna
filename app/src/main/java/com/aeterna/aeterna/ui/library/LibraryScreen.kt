package com.aeterna.aeterna.ui.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aeterna.core.domain.Playlist
import com.aeterna.core.domain.Song

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
        viewModel: LibraryViewModel = hiltViewModel(),
        onSongClick: (Song) -> Unit = {},
        onPlaylistClick: (Playlist) -> Unit = {},
        onCreatePlaylist: () -> Unit = {},
        modifier: Modifier = Modifier
) {
    val libraryState by viewModel.libraryState.collectAsState()
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val tabs = listOf("Playlists", "Liked Songs", "Downloaded")

    Scaffold(
            topBar = {
                TopAppBar(
                        title = {
                            Text(
                                    "Your Library",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold
                            )
                        },
                        actions = {
                            IconButton(onClick = { /* More options */}) {
                                Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = "More options"
                                )
                            }
                        }
                )
            },
            floatingActionButton = {
                if (selectedTabIndex == 0) { // Playlists tab
                    FloatingActionButton(
                            onClick = onCreatePlaylist,
                            containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Create playlist"
                        )
                    }
                }
            },
            modifier = modifier
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            TabRow(selectedTabIndex = selectedTabIndex, modifier = Modifier.fillMaxWidth()) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = {
                                Text(text = title, style = MaterialTheme.typography.labelLarge)
                            }
                    )
                }
            }

            when (selectedTabIndex) {
                0 ->
                        PlaylistsContent(
                                playlists = libraryState.playlists,
                                onPlaylistClick = onPlaylistClick,
                                isLoading = libraryState.isLoading
                        )
                1 ->
                        LikedSongsContent(
                                likedSongs = libraryState.likedSongs,
                                onSongClick = onSongClick,
                                isLoading = libraryState.isLoading
                        )
                2 ->
                        DownloadedContent(
                                downloadedSongs = libraryState.downloadedSongs,
                                onSongClick = onSongClick,
                                isLoading = libraryState.isLoading
                        )
            }
        }
    }
}

@Composable
private fun PlaylistsContent(
        playlists: List<Playlist>,
        onPlaylistClick: (Playlist) -> Unit,
        isLoading: Boolean,
        modifier: Modifier = Modifier
) {
    if (isLoading) {
        LibraryLoadingState()
    } else if (playlists.isEmpty()) {
        EmptyPlaylistsState()
    } else {
        LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(playlists) { playlist ->
                PlaylistCard(playlist = playlist, onClick = { onPlaylistClick(playlist) })
            }
        }
    }
}

@Composable
private fun LikedSongsContent(
        likedSongs: List<Song>,
        onSongClick: (Song) -> Unit,
        isLoading: Boolean,
        modifier: Modifier = Modifier
) {
    if (isLoading) {
        LibraryLoadingState()
    } else if (likedSongs.isEmpty()) {
        EmptyLikedSongsState()
    } else {
        LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(likedSongs) { song -> SongListItem(song = song, onClick = { onSongClick(song) }) }
        }
    }
}

@Composable
private fun DownloadedContent(
        downloadedSongs: List<Song>,
        onSongClick: (Song) -> Unit,
        isLoading: Boolean,
        modifier: Modifier = Modifier
) {
    if (isLoading) {
        LibraryLoadingState()
    } else if (downloadedSongs.isEmpty()) {
        EmptyDownloadedState()
    } else {
        LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(downloadedSongs) { song ->
                SongListItem(song = song, onClick = { onSongClick(song) }, showDownloadIcon = true)
            }
        }
    }
}

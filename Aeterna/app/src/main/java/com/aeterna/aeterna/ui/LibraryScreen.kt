package com.aeterna.aeterna.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.aeterna.core.domain.Playlist
import com.aeterna.core.domain.Song

@Composable
fun LibraryScreen(
        onSongClick: (Song) -> Unit = {},
        onPlaylistClick: (Playlist) -> Unit = {},
        onCreatePlaylist: () -> Unit = {},
        modifier: Modifier = Modifier
) {
    // Simplified implementation for now
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Library functionality coming soon")
    }
}

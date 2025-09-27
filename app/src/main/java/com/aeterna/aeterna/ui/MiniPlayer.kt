package com.aeterna.aeterna.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.semantics.contentDescription
import androidx.compose.foundation.semantics.role
import androidx.compose.foundation.semantics.semantics
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.navigation.NavHostController
import coil.compose.AsyncImage

@Composable
fun MiniPlayer(
        navController: NavHostController,
        modifier: Modifier = Modifier,
        currentSongTitle: String = "Song Title",
        currentArtist: String = "Artist Name",
        isPlaying: Boolean = false,
        progress: Float = 0.5f,
        onPlayPauseClick: () -> Unit = {},
        onMiniPlayerClick: () -> Unit = {},
        thumbnailUrl: String? = null, // New parameter for album art
        highContrastMode: Boolean = false
) {
    Column(
            modifier =
                    modifier.fillMaxWidth()
                            .height(60.dp)
                            .background(
                                    if (highContrastMode) {
                                        MaterialTheme.colorScheme.surface
                                    } else {
                                        MaterialTheme.colorScheme.surfaceVariant
                                    }
                            )
                            .clickable(onClick = onMiniPlayerClick)
                            .semantics {
                                contentDescription =
                                        "Mini player: $currentSongTitle by $currentArtist, ${if (isPlaying) "playing" else "paused"}"
                                role = Role.Button
                            }
    ) {
        LinearProgressIndicator(
                progress = progress,
                modifier =
                        Modifier.fillMaxWidth().semantics {
                            contentDescription =
                                    "Song progress: ${(progress * 100).toInt()}% complete"
                            role = Role.Slider
                        },
                color =
                        if (highContrastMode) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.primary
                        }
        )
        Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
            ) {
                // Placeholder for album art - using AsyncImage instead
                AsyncImage(
                        model = thumbnailUrl,
                        contentDescription = "Album artwork for $currentSongTitle",
                        modifier = Modifier.size(40.dp).clip(MaterialTheme.shapes.small),
                        contentScale = ContentScale.Crop
                )
                Column {
                    Text(
                            text = currentSongTitle,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1
                    )
                    Text(
                            text = currentArtist,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1
                    )
                }
            }
            IconButton(
                    onClick = onPlayPauseClick,
                    modifier =
                            Modifier.size(48.dp).semantics {
                                contentDescription = if (isPlaying) "Pause" else "Play"
                                role = Role.Button
                            }
            ) {
                Icon(
                        imageVector =
                                if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = null, // Handled by parent
                        modifier = Modifier.size(36.dp),
                        tint =
                                if (highContrastMode) {
                                    MaterialTheme.colorScheme.onSurface
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                }
                )
            }
        }
    }
}

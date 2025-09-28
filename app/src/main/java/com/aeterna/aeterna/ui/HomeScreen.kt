package com.aeterna.aeterna.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.semantics.contentDescription
import androidx.compose.foundation.semantics.semantics
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.aeterna.aeterna.ui.theme.MusicShapes
import com.aeterna.aeterna.ui.theme.MusicTypography
import com.aeterna.core.domain.Song
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
        modifier: Modifier = Modifier,
        onNavigateToPlayer: () -> Unit = {},
        onNavigateToPlaylist: (String) -> Unit = {},
        highContrastMode: Boolean = false,
        reduceMotion: Boolean = false
) {
    var isVisible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        isVisible = true
    }
    
    LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item { 
            AnimatedVisibility(
                visible = isVisible,
                enter = if (reduceMotion) fadeIn() else slideInVertically() + fadeIn()
            ) {
                WelcomeSection(highContrastMode = highContrastMode) 
            }
        }
        
        item {
            AnimatedVisibility(
                visible = isVisible,
                enter = if (reduceMotion) fadeIn() else slideInVertically(initialOffsetY = { it / 2 }) + fadeIn()
            ) {
                QuickActionsSection(
                    onShuffleAll = { onNavigateToPlayer() },
                    onPlayFavorites = { onNavigateToPlayer() },
                    highContrastMode = highContrastMode
                )
            }
        }

        item {
            AnimatedVisibility(
                visible = isVisible,
                enter = if (reduceMotion) fadeIn() else slideInVertically(initialOffsetY = { it / 3 }) + fadeIn()
            ) {
                QuickPicksSection(
                        songs = getSampleSongs(),
                        onSongClick = { onNavigateToPlayer() },
                        highContrastMode = highContrastMode,
                        reduceMotion = reduceMotion
                )
            }
        }
        
        item {
            AnimatedVisibility(
                visible = isVisible,
                enter = if (reduceMotion) fadeIn() else slideInVertically(initialOffsetY = { it / 4 }) + fadeIn()
            ) {
                FeaturedPlaylistsSection(
                    playlists = getSamplePlaylists(),
                    onPlaylistClick = { onNavigateToPlaylist(it) },
                    highContrastMode = highContrastMode,
                    reduceMotion = reduceMotion
                )
            }
        }

        item {
            AnimatedVisibility(
                visible = isVisible,
                enter = if (reduceMotion) fadeIn() else slideInVertically(initialOffsetY = { it / 5 }) + fadeIn()
            ) {
                RecentlyPlayedSection(
                        songs = getSampleSongs(),
                        onSongClick = { onNavigateToPlayer() },
                        highContrastMode = highContrastMode,
                        reduceMotion = reduceMotion
                )
            }
        }
        
        item {
            AnimatedVisibility(
                visible = isVisible,
                enter = if (reduceMotion) fadeIn() else slideInVertically(initialOffsetY = { it / 6 }) + fadeIn()
            ) {
                TrendingSection(
                    songs = getSampleSongs().shuffled(),
                    onSongClick = { onNavigateToPlayer() },
                    highContrastMode = highContrastMode,
                    reduceMotion = reduceMotion
                )
            }
        }
        
        item {
            AnimatedVisibility(
                visible = isVisible,
                enter = if (reduceMotion) fadeIn() else slideInVertically(initialOffsetY = { it / 7 }) + fadeIn()
            ) {
                MoodCategoriesSection(
                    categories = getMoodCategories(),
                    onCategoryClick = { onNavigateToPlaylist(it) },
                    highContrastMode = highContrastMode
                )
            }
        }
        
        // Add bottom spacing for mini player
        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
private fun WelcomeSection(highContrastMode: Boolean = false) {
    Column {
        Text(
                text = "Good morning",
                style =
                        MaterialTheme.typography.headlineMedium.copy(
                                fontWeight =
                                        if (highContrastMode) FontWeight.Bold else FontWeight.Bold
                        ),
                modifier =
                        Modifier.semantics { contentDescription = "Welcome message: Good morning" }
        )
        Text(
                text = "What would you like to listen to?",
                style = MaterialTheme.typography.bodyLarge,
                color =
                        if (highContrastMode) {
                            MaterialTheme.colorScheme.onSurface
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                modifier =
                        Modifier.semantics {
                            contentDescription = "What would you like to listen to?"
                        }
        )
    }
}

@Composable
private fun QuickPicksSection(
        songs: List<Song>,
        onSongClick: (Song) -> Unit,
        highContrastMode: Boolean = false,
        reduceMotion: Boolean = false
) {
    Column {
        Text(
                text = "Quick Picks",
                style =
                        MaterialTheme.typography.titleLarge.copy(
                                fontWeight =
                                        if (highContrastMode) FontWeight.Bold
                                        else FontWeight.SemiBold
                        ),
                modifier =
                        Modifier.padding(bottom = 12.dp).semantics {
                            contentDescription = "Quick Picks section"
                        }
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(songs.take(10)) { song ->
                QuickPickCard(
                        song = song,
                        onClick = { onSongClick(song) },
                        highContrastMode = highContrastMode,
                        reduceMotion = reduceMotion
                )
            }
        }
    }
}

@Composable
private fun RecentlyPlayedSection(
        songs: List<Song>,
        onSongClick: (Song) -> Unit,
        highContrastMode: Boolean = false,
        reduceMotion: Boolean = false
) {
    Column {
        Text(
                text = "Recently Played",
                style =
                        MaterialTheme.typography.titleLarge.copy(
                                fontWeight =
                                        if (highContrastMode) FontWeight.Bold
                                        else FontWeight.SemiBold
                        ),
                modifier =
                        Modifier.padding(bottom = 12.dp).semantics {
                            contentDescription = "Recently Played section"
                        }
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(songs.take(8)) { song ->
                RecentlyPlayedCard(
                        song = song,
                        onClick = { onSongClick(song) },
                        highContrastMode = highContrastMode,
                        reduceMotion = reduceMotion
                )
            }
        }
    }
}

@Composable
private fun QuickPickCard(
        song: Song,
        onClick: () -> Unit,
        highContrastMode: Boolean = false,
        reduceMotion: Boolean = false
) {
    Card(
            onClick = onClick,
            modifier =
                    Modifier.size(160.dp).semantics {
                        contentDescription = "Song: ${song.title} by ${song.artist}"
                        role = Role.Button
                    },
            elevation =
                    CardDefaults.cardElevation(
                            defaultElevation = if (highContrastMode) 6.dp else 4.dp
                    ),
            colors =
                    CardDefaults.cardColors(
                            containerColor =
                                    if (highContrastMode) {
                                        MaterialTheme.colorScheme.surface
                                    } else {
                                        CardDefaults.cardColors().containerColor
                                    }
                    )
    ) {
        Column {
            AsyncImage(
                    model = song.thumbnailUrl,
                    contentDescription = "Album artwork for ${song.title}",
                    modifier =
                            Modifier.fillMaxWidth()
                                    .height(120.dp)
                                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)),
                    contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                        text = song.title,
                        style =
                                MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight =
                                                if (highContrastMode) FontWeight.SemiBold
                                                else FontWeight.Medium
                                ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color =
                                if (highContrastMode) {
                                    MaterialTheme.colorScheme.onSurface
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                }
                )
                Text(
                        text = song.artist,
                        style = MaterialTheme.typography.bodySmall,
                        color =
                                if (highContrastMode) {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun RecentlyPlayedCard(
        song: Song,
        onClick: () -> Unit,
        highContrastMode: Boolean = false,
        reduceMotion: Boolean = false
) {
    Card(
            onClick = onClick,
            modifier =
                    Modifier.size(width = 140.dp, height = 180.dp).semantics {
                        contentDescription = "Recently played: ${song.title} by ${song.artist}"
                        role = Role.Button
                    },
            elevation =
                    CardDefaults.cardElevation(
                            defaultElevation = if (highContrastMode) 4.dp else 2.dp
                    ),
            colors =
                    CardDefaults.cardColors(
                            containerColor =
                                    if (highContrastMode) {
                                        MaterialTheme.colorScheme.surface
                                    } else {
                                        CardDefaults.cardColors().containerColor
                                    }
                    )
    ) {
        Column {
            AsyncImage(
                    model = song.thumbnailUrl,
                    contentDescription = "Album artwork for ${song.title}",
                    modifier =
                            Modifier.size(140.dp)
                                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)),
                    contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                        text = song.title,
                        style =
                                MaterialTheme.typography.bodySmall.copy(
                                        fontWeight =
                                                if (highContrastMode) FontWeight.SemiBold
                                                else FontWeight.Medium
                                ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color =
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

private fun getSampleSongs(): List<Song> {
    return listOf(
            Song(
                    id = "1",
                    title = "Blinding Lights",
                    artist = "The Weeknd",
                    album = "After Hours",
                    durationSeconds = 200,
                    thumbnailUrl = "https://example.com/blinding-lights.jpg",
                    videoId = "4NRXx6U8ABQ"
            ),
            Song(
                    id = "2",
                    title = "Watermelon Sugar",
                    artist = "Harry Styles",
                    album = "Fine Line",
                    durationSeconds = 174,
                    thumbnailUrl = "https://example.com/watermelon-sugar.jpg",
                    videoId = "E07s5ZYygMg"
            ),
            Song(
                    id = "3",
                    title = "Good 4 U",
                    artist = "Olivia Rodrigo",
                    album = "SOUR",
                    durationSeconds = 178,
                    thumbnailUrl = "https://example.com/good-4-u.jpg",
                    videoId = "gNi_6U5Pm_o"
            )
    )
}

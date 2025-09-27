package com.aeterna.aeterna.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavHostController
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlin.math.abs

@Composable
fun FullScreenPlayerScreen(
        navController: NavHostController,
        modifier: Modifier = Modifier,
        albumArtUrl: String = "",
        songTitle: String = "Song Title",
        artistName: String = "Artist Name",
        albumName: String = "Album Name",
        isPlaying: Boolean = false,
        currentPosition: Long = 0L,
        totalDuration: Long = 100000L,
        isShuffleEnabled: Boolean = false,
        repeatMode: Int = 0, // 0: off, 1: all, 2: one
        onPlayPauseClick: () -> Unit = {},
        onSkipNextClick: () -> Unit = {},
        onSkipPreviousClick: () -> Unit = {},
        onSeek: (Long) -> Unit = {},
        onShuffleClick: () -> Unit = {},
        onRepeatClick: () -> Unit = {},
        onDismiss: () -> Unit = {}
) {
    val context = LocalContext.current
    val density = LocalDensity.current

    var dominantColor by remember { mutableStateOf(Color.Black) }
    var isUserSeeking by remember { mutableStateOf(false) }
    var seekPosition by remember { mutableFloatStateOf(0f) }

    // Animated dominant color
    val animatedDominantColor by
            animateColorAsState(
                    targetValue = dominantColor,
                    animationSpec = tween(durationMillis = 1000),
                    label = "dominantColorAnimation"
            )

    // Album art rotation for playing state
    val albumRotation by
            animateFloatAsState(
                    targetValue = if (isPlaying) 360f else 0f,
                    animationSpec = tween(durationMillis = 10000),
                    label = "albumRotation"
            )

    // Extract dominant color from album art
    LaunchedEffect(albumArtUrl) {
        if (albumArtUrl.isNotEmpty()) {
            try {
                val request =
                        ImageRequest.Builder(context).data(albumArtUrl).allowHardware(false).build()
                val result = context.imageLoader.execute(request)
                if (result is SuccessResult) {
                    val bitmap = result.drawable.toBitmap()
                    Palette.from(bitmap).generate { palette ->
                        palette?.dominantSwatch?.rgb?.let { colorValue ->
                            dominantColor = Color(colorValue)
                        }
                    }
                }
            } catch (e: Exception) {
                // Fallback to default color
            }
        }
    }

    // Gesture handling for swipe to dismiss
    var dragOffset by remember { mutableFloatStateOf(0f) }
    val dismissThreshold = with(density) { 200.dp.toPx() }

    Box(
            modifier =
                    modifier.fillMaxSize()
                            .background(
                                    Brush.verticalGradient(
                                            colors =
                                                    listOf(
                                                            animatedDominantColor.copy(
                                                                    alpha = 0.3f
                                                            ),
                                                            Color.Black.copy(alpha = 0.8f),
                                                            Color.Black
                                                    )
                                    )
                            )
                            .pointerInput(Unit) {
                                detectDragGestures(
                                        onDragEnd = {
                                            if (abs(dragOffset) > dismissThreshold) {
                                                onDismiss()
                                            }
                                            dragOffset = 0f
                                        }
                                ) { _, dragAmount ->
                                    if (dragAmount.y > 0) {
                                        dragOffset = dragAmount.y
                                    }
                                }
                            }
                            .graphicsLayer {
                                translationY = dragOffset.coerceAtLeast(0f)
                                alpha =
                                        1f -
                                                (dragOffset / (dismissThreshold * 2)).coerceIn(
                                                        0f,
                                                        0.3f
                                                )
                            }
    ) {
        // Blurred background
        AsyncImage(
                model = albumArtUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize().blur(50.dp),
                contentScale = ContentScale.Crop,
                alpha = 0.3f
        )

        Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top bar
            Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Dismiss",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                    )
                }

                Text(
                        text = "Now Playing",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White.copy(alpha = 0.8f),
                        fontWeight = FontWeight.Medium
                )

                IconButton(onClick = { /* More options */}) {
                    Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More options",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Album Art with rotation and scaling
            Card(
                    modifier =
                            Modifier.size(320.dp)
                                    .rotate(if (isPlaying) albumRotation else 0f)
                                    .scale(
                                            animateFloatAsState(
                                                            targetValue =
                                                                    if (isPlaying) 1.02f else 1f,
                                                            label = "albumScale"
                                                    )
                                                    .value
                                    ),
                    shape = RoundedCornerShape(16.dp)
            ) {
                AsyncImage(
                        model = albumArtUrl,
                        contentDescription = "Album Art",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Song Info
            Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                        text = songTitle,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                        text = artistName,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                )

                if (albumName.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                            text = albumName,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White.copy(alpha = 0.6f),
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Progress Section
            Column {
                // Progress Slider with gesture handling
                Slider(
                        value = if (isUserSeeking) seekPosition else currentPosition.toFloat(),
                        onValueChange = { value ->
                            isUserSeeking = true
                            seekPosition = value
                        },
                        onValueChangeFinished = {
                            onSeek(seekPosition.toLong())
                            isUserSeeking = false
                        },
                        valueRange = 0f..totalDuration.toFloat(),
                        modifier =
                                Modifier.fillMaxWidth().pointerInput(Unit) {
                                    detectTapGestures { offset ->
                                        val progress = offset.x / size.width
                                        val newPosition = progress * totalDuration
                                        onSeek(newPosition.toLong())
                                    }
                                },
                        colors =
                                SliderDefaults.colors(
                                        thumbColor = Color.White,
                                        activeTrackColor = Color.White,
                                        inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                                )
                )

                // Time indicators
                Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                            text = formatTime(currentPosition),
                            color = Color.White.copy(alpha = 0.8f),
                            style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                            text = formatTime(totalDuration),
                            color = Color.White.copy(alpha = 0.8f),
                            style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Control buttons
            Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
            ) {
                // Shuffle
                IconButton(onClick = onShuffleClick, modifier = Modifier.size(48.dp)) {
                    Icon(
                            imageVector = Icons.Default.Shuffle,
                            contentDescription = "Shuffle",
                            tint =
                                    if (isShuffleEnabled) animatedDominantColor
                                    else Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.size(24.dp)
                    )
                }

                // Previous
                IconButton(onClick = onSkipPreviousClick, modifier = Modifier.size(64.dp)) {
                    Icon(
                            imageVector = Icons.Default.SkipPrevious,
                            contentDescription = "Previous",
                            tint = Color.White,
                            modifier = Modifier.size(40.dp)
                    )
                }

                // Play/Pause
                Surface(
                        onClick = onPlayPauseClick,
                        modifier = Modifier.size(80.dp),
                        shape = CircleShape,
                        color = Color.White
                ) {
                    Icon(
                            imageVector =
                                    if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            tint = animatedDominantColor,
                            modifier = Modifier.size(48.dp).padding(12.dp)
                    )
                }

                // Next
                IconButton(onClick = onSkipNextClick, modifier = Modifier.size(64.dp)) {
                    Icon(
                            imageVector = Icons.Default.SkipNext,
                            contentDescription = "Next",
                            tint = Color.White,
                            modifier = Modifier.size(40.dp)
                    )
                }

                // Repeat
                IconButton(onClick = onRepeatClick, modifier = Modifier.size(48.dp)) {
                    Icon(
                            imageVector = Icons.Default.Repeat,
                            contentDescription = "Repeat",
                            tint =
                                    when (repeatMode) {
                                        0 -> Color.White.copy(alpha = 0.7f)
                                        else -> animatedDominantColor
                                    },
                            modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

private fun formatTime(milliseconds: Long): String {
    val minutes = (milliseconds / 1000) / 60
    val seconds = (milliseconds / 1000) % 60
    return String.format("%02d:%02d", minutes, seconds)
}

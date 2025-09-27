package com.aeterna.aeterna.ui.lyrics

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class LyricLine(
        val startTime: Long, // in milliseconds
        val endTime: Long,
        val text: String
)

@Composable
fun LyricsPanel(
        lyrics: List<LyricLine>,
        currentPosition: Long,
        dominantColor: Color,
        modifier: Modifier = Modifier,
        onLyricClick: (Long) -> Unit = {}
) {
    val listState = rememberLazyListState()

    // Find current lyric index
    val currentLyricIndex by
            remember(currentPosition) {
                derivedStateOf {
                    lyrics
                            .indexOfFirst { lyric ->
                                currentPosition >= lyric.startTime &&
                                        currentPosition < lyric.endTime
                            }
                            .takeIf { it >= 0 }
                            ?: -1
                }
            }

    // Auto-scroll to current lyric
    LaunchedEffect(currentLyricIndex) {
        if (currentLyricIndex >= 0 && lyrics.isNotEmpty()) {
            listState.animateScrollToItem(index = (currentLyricIndex - 1).coerceAtLeast(0))
        }
    }

    if (lyrics.isEmpty()) {
        LyricsEmptyState(modifier = modifier)
    } else {
        LazyColumn(
                state = listState,
                modifier = modifier.fillMaxSize().padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Add some top padding
            item { Box(modifier = Modifier.padding(top = 32.dp)) }

            itemsIndexed(lyrics) { index, lyric ->
                LyricLineItem(
                        lyric = lyric,
                        isActive = index == currentLyricIndex,
                        dominantColor = dominantColor,
                        onClick = { onLyricClick(lyric.startTime) }
                )
            }

            // Add some bottom padding
            item { Box(modifier = Modifier.padding(bottom = 32.dp)) }
        }
    }
}

@Composable
private fun LyricLineItem(
        lyric: LyricLine,
        isActive: Boolean,
        dominantColor: Color,
        onClick: () -> Unit,
        modifier: Modifier = Modifier
) {
    val textColor by
            animateColorAsState(
                    targetValue =
                            if (isActive) {
                                dominantColor
                            } else {
                                Color.White.copy(alpha = 0.6f)
                            },
                    animationSpec = tween(300),
                    label = "lyricColorAnimation"
            )

    val backgroundColor by
            animateColorAsState(
                    targetValue =
                            if (isActive) {
                                dominantColor.copy(alpha = 0.1f)
                            } else {
                                Color.Transparent
                            },
                    animationSpec = tween(300),
                    label = "lyricBackgroundAnimation"
            )

    Surface(
            onClick = onClick,
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = backgroundColor
    ) {
        Text(
                text = lyric.text,
                style =
                        if (isActive) {
                            MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp
                            )
                        } else {
                            MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp)
                        },
                color = textColor,
                textAlign = TextAlign.Center,
                modifier =
                        Modifier.padding(
                                horizontal = 16.dp,
                                vertical = if (isActive) 12.dp else 8.dp
                        )
        )
    }
}

@Composable
private fun LyricsEmptyState(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                    text = "🎵",
                    style = MaterialTheme.typography.displayLarge,
                    color = Color.White.copy(alpha = 0.6f)
            )
            Text(
                    text = "No lyrics available",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Medium
            )
            Text(
                    text = "Enjoy the music",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.6f)
            )
        }
    }
}

// Sample lyrics data for testing
fun getSampleLyrics(): List<LyricLine> {
    return listOf(
            LyricLine(0, 5000, "This is the first line of the song"),
            LyricLine(5000, 10000, "Here comes the second line"),
            LyricLine(10000, 15000, "And this is the third line"),
            LyricLine(15000, 20000, "The chorus starts here"),
            LyricLine(
                    20000,
                    25000,
                    "This is a longer line that might wrap to multiple lines in the UI"
            ),
            LyricLine(25000, 30000, "Another verse begins"),
            LyricLine(30000, 35000, "The music flows"),
            LyricLine(35000, 40000, "And the lyrics continue"),
            LyricLine(40000, 45000, "Building up to something great"),
            LyricLine(45000, 50000, "The finale approaches"),
            LyricLine(50000, 55000, "And this is the end")
    )
}

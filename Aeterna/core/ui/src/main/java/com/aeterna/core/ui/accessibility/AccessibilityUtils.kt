package com.aeterna.core.ui.accessibility

import android.content.Context
import android.view.accessibility.AccessibilityManager
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.semantics.contentDescription
import androidx.compose.foundation.semantics.semantics
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Accessibility utilities for the Aeterna app Provides semantic descriptions, roles, and
 * accessibility-aware components
 */
object AccessibilityUtils {

    /** Check if TalkBack or other accessibility services are enabled */
    fun isAccessibilityEnabled(context: Context): Boolean {
        val accessibilityManager =
                context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        return accessibilityManager.isEnabled && accessibilityManager.isTouchExplorationEnabled
    }

    /** Get accessible color with improved contrast */
    @Composable
    fun getAccessibleColor(
            foreground: Color,
            background: Color,
            minimumContrast: Float = 4.5f
    ): Color {
        val contrast = calculateContrastRatio(foreground, background)
        return if (contrast >= minimumContrast) {
            foreground
        } else {
            // Adjust color for better contrast
            if (isColorLight(background)) {
                foreground.copy(
                        red = foreground.red * 0.7f,
                        green = foreground.green * 0.7f,
                        blue = foreground.blue * 0.7f
                )
            } else {
                foreground.copy(
                        red = minOf(1f, foreground.red * 1.3f),
                        green = minOf(1f, foreground.green * 1.3f),
                        blue = minOf(1f, foreground.blue * 1.3f)
                )
            }
        }
    }

    /** Calculate contrast ratio between two colors */
    private fun calculateContrastRatio(color1: Color, color2: Color): Float {
        val l1 = getLuminance(color1)
        val l2 = getLuminance(color2)
        val lighter = maxOf(l1, l2)
        val darker = minOf(l1, l2)
        return (lighter + 0.05f) / (darker + 0.05f)
    }

    /** Get luminance of a color */
    private fun getLuminance(color: Color): Float {
        val r =
                if (color.red <= 0.03928f) color.red / 12.92f
                else kotlin.math.pow((color.red + 0.055f) / 1.055f, 2.4f).toFloat()
        val g =
                if (color.green <= 0.03928f) color.green / 12.92f
                else kotlin.math.pow((color.green + 0.055f) / 1.055f, 2.4f).toFloat()
        val b =
                if (color.blue <= 0.03928f) color.blue / 12.92f
                else kotlin.math.pow((color.blue + 0.055f) / 1.055f, 2.4f).toFloat()
        return 0.2126f * r + 0.7152f * g + 0.0722f * b
    }

    /** Check if color is light */
    private fun isColorLight(color: Color): Boolean {
        return getLuminance(color) > 0.5f
    }

    /** Format duration for accessibility */
    fun formatDurationForAccessibility(durationMs: Long): String {
        val seconds = (durationMs / 1000) % 60
        val minutes = (durationMs / (1000 * 60)) % 60
        val hours = (durationMs / (1000 * 60 * 60)) % 24

        return when {
            hours > 0 -> "$hours hours, $minutes minutes, $seconds seconds"
            minutes > 0 -> "$minutes minutes, $seconds seconds"
            else -> "$seconds seconds"
        }
    }

    /** Get accessible touch target size */
    val MinimumTouchTargetSize: Dp = 48.dp

    /** Modifier for accessible touch targets */
    @Composable
    fun Modifier.accessibleTouchTarget(description: String, role: Role = Role.Button): Modifier =
            this.size(MinimumTouchTargetSize).semantics {
                contentDescription = description
                this.role = role
            }
}

/** Semantic modifiers for common UI patterns */
object SemanticModifiers {

    @Composable
    fun playButton(isPlaying: Boolean): Modifier =
            Modifier.semantics {
                contentDescription = if (isPlaying) "Pause" else "Play"
                role = Role.Button
            }

    @Composable
    fun skipButton(direction: String): Modifier =
            Modifier.semantics {
                contentDescription = "Skip $direction"
                role = Role.Button
            }

    @Composable
    fun songItem(
            title: String,
            artist: String,
            duration: String,
            isPlaying: Boolean = false
    ): Modifier =
            Modifier.semantics {
                contentDescription = buildString {
                    append("Song: $title by $artist, duration $duration")
                    if (isPlaying) append(", currently playing")
                }
                role = Role.Button
            }

    @Composable
    fun playlistItem(name: String, songCount: Int, isPrivate: Boolean = false): Modifier =
            Modifier.semantics {
                contentDescription = buildString {
                    append("Playlist: $name, $songCount songs")
                    if (isPrivate) append(", private")
                }
                role = Role.Button
            }

    @Composable
    fun seekBar(currentPosition: String, duration: String, progress: Float): Modifier =
            Modifier.semantics {
                contentDescription =
                        "Seek bar, current position $currentPosition of $duration, ${(progress * 100).toInt()}% complete"
                role = Role.Slider
            }
}

package com.aeterna.aeterna.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.aeterna.core.ui.accessibility.AccessibilityUtils

/** Accessibility-aware theme system that adapts to user preferences */
data class AccessibilityPreferences(
        val highContrastMode: Boolean = false,
        val reduceMotion: Boolean = false,
        val largeText: Boolean = false,
        val hapticFeedback: Boolean = true
)

val LocalAccessibilityPreferences = staticCompositionLocalOf { AccessibilityPreferences() }

@Composable
fun AccessibilityAeternalTheme(
        darkTheme: Boolean = isSystemInDarkTheme(),
        accessibilityPreferences: AccessibilityPreferences = AccessibilityPreferences(),
        content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val isAccessibilityEnabled = AccessibilityUtils.isAccessibilityEnabled(context)

    // Enhanced color scheme for high contrast mode
    val colorScheme =
            when {
                accessibilityPreferences.highContrastMode && darkTheme ->
                        createHighContrastDarkColorScheme()
                accessibilityPreferences.highContrastMode && !darkTheme ->
                        createHighContrastLightColorScheme()
                darkTheme -> dynamicDarkColorScheme(context)
                else -> dynamicLightColorScheme(context)
            }

    // Enhanced typography for large text mode
    val typography =
            if (accessibilityPreferences.largeText) {
                createLargeTextTypography()
            } else {
                Typography()
            }

    // Provide accessibility preferences to all composables
    CompositionLocalProvider(LocalAccessibilityPreferences provides accessibilityPreferences) {
        MaterialTheme(colorScheme = colorScheme, typography = typography, content = content)
    }
}

@Composable
private fun createHighContrastDarkColorScheme(): ColorScheme {
    return darkColorScheme(
            primary = Color(0xFF64B5F6), // Lighter blue for better contrast
            onPrimary = Color.Black,
            primaryContainer = Color(0xFF1976D2),
            onPrimaryContainer = Color.White,
            secondary = Color(0xFFFFB74D), // Light orange
            onSecondary = Color.Black,
            secondaryContainer = Color(0xFFFF8F00),
            onSecondaryContainer = Color.White,
            tertiary = Color(0xFFA5D6A7), // Light green
            onTertiary = Color.Black,
            tertiaryContainer = Color(0xFF388E3C),
            onTertiaryContainer = Color.White,
            error = Color(0xFFFF5252),
            onError = Color.Black,
            errorContainer = Color(0xFFD32F2F),
            onErrorContainer = Color.White,
            background = Color.Black,
            onBackground = Color.White,
            surface = Color(0xFF121212),
            onSurface = Color.White,
            surfaceVariant = Color(0xFF2C2C2C),
            onSurfaceVariant = Color(0xFFE0E0E0),
            outline = Color(0xFF757575),
            outlineVariant = Color(0xFF424242)
    )
}

@Composable
private fun createHighContrastLightColorScheme(): ColorScheme {
    return lightColorScheme(
            primary = Color(0xFF1565C0), // Darker blue for better contrast
            onPrimary = Color.White,
            primaryContainer = Color(0xFFE3F2FD),
            onPrimaryContainer = Color.Black,
            secondary = Color(0xFFE65100), // Dark orange
            onSecondary = Color.White,
            secondaryContainer = Color(0xFFFFF3E0),
            onSecondaryContainer = Color.Black,
            tertiary = Color(0xFF2E7D32), // Dark green
            onTertiary = Color.White,
            tertiaryContainer = Color(0xFFE8F5E8),
            onTertiaryContainer = Color.Black,
            error = Color(0xFFD32F2F),
            onError = Color.White,
            errorContainer = Color(0xFFFFEBEE),
            onErrorContainer = Color.Black,
            background = Color.White,
            onBackground = Color.Black,
            surface = Color(0xFFFAFAFA),
            onSurface = Color.Black,
            surfaceVariant = Color(0xFFF5F5F5),
            onSurfaceVariant = Color(0xFF424242),
            outline = Color(0xFF757575),
            outlineVariant = Color(0xFFBDBDBD)
    )
}

@Composable
private fun createLargeTextTypography(): Typography {
    val defaultTypography = Typography()
    return Typography(
            displayLarge =
                    defaultTypography.displayLarge.copy(
                            fontSize = defaultTypography.displayLarge.fontSize * 1.2f
                    ),
            displayMedium =
                    defaultTypography.displayMedium.copy(
                            fontSize = defaultTypography.displayMedium.fontSize * 1.2f
                    ),
            displaySmall =
                    defaultTypography.displaySmall.copy(
                            fontSize = defaultTypography.displaySmall.fontSize * 1.2f
                    ),
            headlineLarge =
                    defaultTypography.headlineLarge.copy(
                            fontSize = defaultTypography.headlineLarge.fontSize * 1.2f
                    ),
            headlineMedium =
                    defaultTypography.headlineMedium.copy(
                            fontSize = defaultTypography.headlineMedium.fontSize * 1.2f
                    ),
            headlineSmall =
                    defaultTypography.headlineSmall.copy(
                            fontSize = defaultTypography.headlineSmall.fontSize * 1.2f
                    ),
            titleLarge =
                    defaultTypography.titleLarge.copy(
                            fontSize = defaultTypography.titleLarge.fontSize * 1.2f
                    ),
            titleMedium =
                    defaultTypography.titleMedium.copy(
                            fontSize = defaultTypography.titleMedium.fontSize * 1.2f
                    ),
            titleSmall =
                    defaultTypography.titleSmall.copy(
                            fontSize = defaultTypography.titleSmall.fontSize * 1.2f
                    ),
            bodyLarge =
                    defaultTypography.bodyLarge.copy(
                            fontSize = defaultTypography.bodyLarge.fontSize * 1.2f
                    ),
            bodyMedium =
                    defaultTypography.bodyMedium.copy(
                            fontSize = defaultTypography.bodyMedium.fontSize * 1.2f
                    ),
            bodySmall =
                    defaultTypography.bodySmall.copy(
                            fontSize = defaultTypography.bodySmall.fontSize * 1.2f
                    ),
            labelLarge =
                    defaultTypography.labelLarge.copy(
                            fontSize = defaultTypography.labelLarge.fontSize * 1.2f
                    ),
            labelMedium =
                    defaultTypography.labelMedium.copy(
                            fontSize = defaultTypography.labelMedium.fontSize * 1.2f
                    ),
            labelSmall =
                    defaultTypography.labelSmall.copy(
                            fontSize = defaultTypography.labelSmall.fontSize * 1.2f
                    )
    )
}

/** Extension function to get accessibility preferences from composition local */
@Composable
fun getAccessibilityPreferences(): AccessibilityPreferences {
    return LocalAccessibilityPreferences.current
}

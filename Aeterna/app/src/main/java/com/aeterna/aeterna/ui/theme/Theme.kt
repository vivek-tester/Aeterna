package com.aeterna.aeterna.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme =
        lightColorScheme(
                primary = PrimaryLight,
                onPrimary = OnPrimaryLight,
                primaryContainer = PrimaryContainerLight,
                onPrimaryContainer = OnPrimaryContainerLight,
                secondary = SecondaryLight,
                onSecondary = OnSecondaryLight,
                secondaryContainer = SecondaryContainerLight,
                onSecondaryContainer = OnSecondaryContainerLight,
                tertiary = TertiaryLight,
                onTertiary = OnTertiaryLight,
                tertiaryContainer = TertiaryContainerLight,
                onTertiaryContainer = OnTertiaryContainerLight,
                error = ErrorLight,
                errorContainer = ErrorContainerLight,
                background = BackgroundLight,
                onBackground = OnBackgroundLight,
                surface = SurfaceLight,
                onSurface = OnSurfaceLight,
                surfaceVariant = SurfaceVariantLight,
                onSurfaceVariant = OnSurfaceVariantLight,
                outline = OutlineLight
        )

private val DarkColorScheme =
        darkColorScheme(
                primary = PrimaryDark,
                onPrimary = OnPrimaryDark,
                primaryContainer = PrimaryContainerDark,
                onPrimaryContainer = OnPrimaryContainerDark,
                secondary = SecondaryDark,
                onSecondary = OnSecondaryDark,
                secondaryContainer = SecondaryContainerDark,
                onSecondaryContainer = OnSecondaryContainerDark,
                tertiary = TertiaryDark,
                onTertiary = OnTertiaryDark,
                tertiaryContainer = TertiaryContainerDark,
                onTertiaryContainer = OnTertiaryContainerDark,
                error = ErrorDark,
                errorContainer = ErrorContainerDark,
                background = BackgroundDark,
                onBackground = OnBackgroundDark,
                surface = SurfaceDark,
                onSurface = OnSurfaceDark,
                surfaceVariant = SurfaceVariantDark,
                onSurfaceVariant = OnSurfaceVariantDark,
                outline = OutlineDark
        )

private val PureBlackColorScheme =
        darkColorScheme(
                primary = PrimaryPureBlack,
                onPrimary = OnPrimaryPureBlack,
                primaryContainer = PrimaryContainerPureBlack,
                onPrimaryContainer = OnPrimaryContainerPureBlack,
                secondary = SecondaryPureBlack,
                onSecondary = OnSecondaryPureBlack,
                secondaryContainer = SecondaryContainerPureBlack,
                onSecondaryContainer = OnSecondaryContainerPureBlack,
                tertiary = TertiaryDark,
                onTertiary = OnTertiaryDark,
                tertiaryContainer = TertiaryContainerDark,
                onTertiaryContainer = OnTertiaryContainerDark,
                error = ErrorDark,
                errorContainer = ErrorContainerDark,
                background = BackgroundPureBlack,
                onBackground = OnBackgroundPureBlack,
                surface = SurfacePureBlack,
                onSurface = OnSurfacePureBlack,
                surfaceVariant = SurfaceVariantPureBlack,
                onSurfaceVariant = OnSurfaceVariantPureBlack,
                outline = OutlineDark
        )

@Composable
fun AeternaTheme(
        darkTheme: Boolean = isSystemInDarkTheme(),
        dynamicColor: Boolean = true,
        pureBlack: Boolean = false,
        content: @Composable () -> Unit
) {
    val context = LocalContext.current

    val colorScheme =
            when {
                dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                    if (darkTheme) {
                        dynamicDarkColorScheme(context)
                    } else {
                        dynamicLightColorScheme(context)
                    }
                }
                pureBlack -> PureBlackColorScheme
                darkTheme -> DarkColorScheme
                else -> LightColorScheme
            }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.surface.toArgb()
            window.navigationBarColor = colorScheme.surface.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = Shapes,
            content = content
    )
}

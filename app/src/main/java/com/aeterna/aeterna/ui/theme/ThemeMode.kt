package com.aeterna.aeterna.ui.theme

/**
 * Represents the available theme modes for the application
 */
enum class ThemeMode {
    LIGHT,
    DARK,
    PURE_BLACK, // AMOLED-friendly pure black theme
    SYSTEM,     // Follow system theme
    DYNAMIC     // Material You dynamic theme
}

/**
 * Represents contrast preferences for accessibility
 */
enum class ThemeContrast {
    STANDARD,
    MEDIUM,
    HIGH
}

/**
 * Theme configuration data class
 */
data class ThemeConfig(
    val mode: ThemeMode = ThemeMode.SYSTEM,
    val dynamicColor: Boolean = true,
    val contrast: ThemeContrast = ThemeContrast.STANDARD,
    val monochromeAccent: Boolean = false,
    val customPrimaryColor: Long? = null // Custom primary color as Color.value
)
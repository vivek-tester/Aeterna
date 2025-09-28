package com.aeterna.aeterna.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Material 3 Shape System for Music App
val Shapes =
        Shapes(
                // Extra Small - for small components like chips, badges
                extraSmall = RoundedCornerShape(4.dp),

                // Small - for buttons, cards
                small = RoundedCornerShape(8.dp),

                // Medium - for dialogs, bottom sheets
                medium = RoundedCornerShape(12.dp),

                // Large - for large surfaces, containers
                large = RoundedCornerShape(16.dp),

                // Extra Large - for prominent surfaces
                extraLarge = RoundedCornerShape(28.dp)
        )

// Custom shapes for music app specific components
val MusicShapes =
        object {
            // Player specific shapes
            val playerCard = RoundedCornerShape(20.dp)
            val miniPlayer =
                    RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = 0.dp,
                            bottomEnd = 0.dp
                    )
            val albumArt = RoundedCornerShape(12.dp)
            val albumArtLarge = RoundedCornerShape(16.dp)

            // UI Component shapes
            val searchBar = RoundedCornerShape(24.dp)
            val chip = RoundedCornerShape(16.dp)
            val categoryCard = RoundedCornerShape(12.dp)
            val playlistCard = RoundedCornerShape(8.dp)

            // Progressive disclosure shapes
            val bottomSheet =
                    RoundedCornerShape(
                            topStart = 20.dp,
                            topEnd = 20.dp,
                            bottomStart = 0.dp,
                            bottomEnd = 0.dp
                    )
            val modal = RoundedCornerShape(20.dp)

            // Progress and control shapes
            val progressBar = RoundedCornerShape(2.dp)
            val slider = RoundedCornerShape(16.dp)
            val button = RoundedCornerShape(24.dp)
            val toggleButton = RoundedCornerShape(20.dp)
        }

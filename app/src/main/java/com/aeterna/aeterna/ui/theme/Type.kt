package com.aeterna.aeterna.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Enhanced Material 3 Typography System for Music App
val Typography =
        Typography(
                // Display styles - for large titles and hero text
                displayLarge =
                        TextStyle(
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Normal,
                                fontSize = 57.sp,
                                lineHeight = 64.sp,
                                letterSpacing = (-0.25).sp
                        ),
                displayMedium =
                        TextStyle(
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Normal,
                                fontSize = 45.sp,
                                lineHeight = 52.sp,
                                letterSpacing = 0.sp
                        ),
                displaySmall =
                        TextStyle(
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Normal,
                                fontSize = 36.sp,
                                lineHeight = 44.sp,
                                letterSpacing = 0.sp
                        ),

                // Headline styles - for section headers and important text
                headlineLarge =
                        TextStyle(
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Normal,
                                fontSize = 32.sp,
                                lineHeight = 40.sp,
                                letterSpacing = 0.sp
                        ),
                headlineMedium =
                        TextStyle(
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Normal,
                                fontSize = 28.sp,
                                lineHeight = 36.sp,
                                letterSpacing = 0.sp
                        ),
                headlineSmall =
                        TextStyle(
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Normal,
                                fontSize = 24.sp,
                                lineHeight = 32.sp,
                                letterSpacing = 0.sp
                        ),

                // Title styles - for card titles, dialog headers
                titleLarge =
                        TextStyle(
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Medium,
                                fontSize = 22.sp,
                                lineHeight = 28.sp,
                                letterSpacing = 0.sp
                        ),
                titleMedium =
                        TextStyle(
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp,
                                lineHeight = 24.sp,
                                letterSpacing = 0.15.sp
                        ),
                titleSmall =
                        TextStyle(
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                letterSpacing = 0.1.sp
                        ),

                // Body styles - for main content text
                bodyLarge =
                        TextStyle(
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp,
                                lineHeight = 24.sp,
                                letterSpacing = 0.5.sp
                        ),
                bodyMedium =
                        TextStyle(
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                letterSpacing = 0.25.sp
                        ),
                bodySmall =
                        TextStyle(
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp,
                                lineHeight = 16.sp,
                                letterSpacing = 0.4.sp
                        ),

                // Label styles - for buttons, chips, form fields
                labelLarge =
                        TextStyle(
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                letterSpacing = 0.1.sp
                        ),
                labelMedium =
                        TextStyle(
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp,
                                lineHeight = 16.sp,
                                letterSpacing = 0.5.sp
                        ),
                labelSmall =
                        TextStyle(
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Medium,
                                fontSize = 11.sp,
                                lineHeight = 16.sp,
                                letterSpacing = 0.5.sp
                        )
        )

// Custom typography extensions for music app specific use cases
val MusicTypography =
        object {
            val songTitle =
                    TextStyle(
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            letterSpacing = 0.sp
                    )

            val artistName =
                    TextStyle(
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            letterSpacing = 0.25.sp
                    )

            val albumTitle =
                    TextStyle(
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Medium,
                            fontSize = 18.sp,
                            lineHeight = 24.sp,
                            letterSpacing = 0.sp
                    )

            val playlistTitle =
                    TextStyle(
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 20.sp,
                            lineHeight = 28.sp,
                            letterSpacing = 0.sp
                    )

            val sectionHeader =
                    TextStyle(
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            lineHeight = 28.sp,
                            letterSpacing = 0.sp
                    )

            val playerTime =
                    TextStyle(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            letterSpacing = 0.4.sp
                    )

            val lyricsText =
                    TextStyle(
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                            lineHeight = 28.sp,
                            letterSpacing = 0.sp
                    )

            val lyricsHighlight =
                    TextStyle(
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            lineHeight = 28.sp,
                            letterSpacing = 0.sp
                    )
        }

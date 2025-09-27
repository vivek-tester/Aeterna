package com.aeterna.core.ui.accessibility

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/** Accessibility-aware components with high contrast mode support */
@Composable
fun AccessibleCard(
        modifier: Modifier = Modifier,
        onClick: (() -> Unit)? = null,
        contentDescription: String? = null,
        highContrast: Boolean = false,
        content: @Composable ColumnScope.() -> Unit
) {
    val colors =
            if (highContrast) {
                CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                )
            } else {
                CardDefaults.cardColors()
            }

    Card(
            modifier =
                    modifier.fillMaxWidth()
                            .let { mod ->
                                contentDescription?.let { desc ->
                                    mod.semantics { this.contentDescription = desc }
                                }
                                        ?: mod
                            }
                            .let { mod ->
                                onClick?.let { clickAction -> mod.clickable { clickAction() } }
                                        ?: mod
                            },
            colors = colors,
            elevation =
                    CardDefaults.cardElevation(defaultElevation = if (highContrast) 8.dp else 4.dp),
            shape = RoundedCornerShape(12.dp),
            content = content
    )
}

@Composable
fun AccessibleButton(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        contentDescription: String? = null,
        highContrast: Boolean = false,
        content: @Composable RowScope.() -> Unit
) {
    val colors =
            if (highContrast) {
                ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                ButtonDefaults.buttonColors()
            }

    Button(
            onClick = onClick,
            modifier =
                    modifier.heightIn(min = AccessibilityUtils.MinimumTouchTargetSize).let { mod ->
                        contentDescription?.let { desc ->
                            mod.semantics { this.contentDescription = desc }
                        }
                                ?: mod
                    },
            enabled = enabled,
            colors = colors,
            elevation =
                    ButtonDefaults.buttonElevation(
                            defaultElevation = if (highContrast) 6.dp else 2.dp
                    ),
            content = content
    )
}

@Composable
fun AccessibleIconButton(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        contentDescription: String,
        highContrast: Boolean = false,
        content: @Composable () -> Unit
) {
    val colors =
            if (highContrast) {
                IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            } else {
                IconButtonDefaults.iconButtonColors()
            }

    IconButton(
            onClick = onClick,
            modifier =
                    modifier.size(AccessibilityUtils.MinimumTouchTargetSize).semantics {
                        this.contentDescription = contentDescription
                    },
            enabled = enabled,
            colors = colors,
            content = content
    )
}

@Composable
fun AccessibleText(
        text: String,
        modifier: Modifier = Modifier,
        style: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyMedium,
        color: Color = Color.Unspecified,
        highContrast: Boolean = false,
        contentDescription: String? = null
) {
    val context = LocalContext.current
    val backgroundColor = MaterialTheme.colorScheme.background

    val accessibleColor =
            if (highContrast && color != Color.Unspecified) {
                AccessibilityUtils.getAccessibleColor(
                        foreground = color,
                        background = backgroundColor,
                        minimumContrast = 7.0f // Higher contrast for high contrast mode
                )
            } else if (color != Color.Unspecified) {
                AccessibilityUtils.getAccessibleColor(
                        foreground = color,
                        background = backgroundColor,
                        minimumContrast = 4.5f
                )
            } else {
                color
            }

    Text(
            text = text,
            modifier =
                    modifier.let { mod ->
                        contentDescription?.let { desc ->
                            mod.semantics { this.contentDescription = desc }
                        }
                                ?: mod
                    },
            style =
                    style.copy(
                            fontWeight = if (highContrast) FontWeight.SemiBold else style.fontWeight
                    ),
            color = accessibleColor
    )
}

@Composable
fun AccessibilitySettings(
        modifier: Modifier = Modifier,
        onHighContrastToggle: (Boolean) -> Unit,
        onReduceMotionToggle: (Boolean) -> Unit,
        onLargeTextToggle: (Boolean) -> Unit,
        highContrastEnabled: Boolean = false,
        reduceMotionEnabled: Boolean = false,
        largeTextEnabled: Boolean = false
) {
    Column(modifier = modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
                text = "Accessibility Options",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
        )

        // High Contrast Mode
        Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "High Contrast Mode", style = MaterialTheme.typography.bodyLarge)
                Text(
                        text = "Increases contrast for better visibility",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Switch(
                    checked = highContrastEnabled,
                    onCheckedChange = onHighContrastToggle,
                    modifier =
                            Modifier.semantics {
                                contentDescription =
                                        "High contrast mode, ${if (highContrastEnabled) "enabled" else "disabled"}"
                            }
            )
        }

        Divider()

        // Reduce Motion
        Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Reduce Motion", style = MaterialTheme.typography.bodyLarge)
                Text(
                        text = "Minimizes animations and transitions",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Switch(
                    checked = reduceMotionEnabled,
                    onCheckedChange = onReduceMotionToggle,
                    modifier =
                            Modifier.semantics {
                                contentDescription =
                                        "Reduce motion, ${if (reduceMotionEnabled) "enabled" else "disabled"}"
                            }
            )
        }

        Divider()

        // Large Text
        Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Large Text", style = MaterialTheme.typography.bodyLarge)
                Text(
                        text = "Increases text size for better readability",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Switch(
                    checked = largeTextEnabled,
                    onCheckedChange = onLargeTextToggle,
                    modifier =
                            Modifier.semantics {
                                contentDescription =
                                        "Large text, ${if (largeTextEnabled) "enabled" else "disabled"}"
                            }
            )
        }
    }
}

package com.aeterna.aeterna.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aeterna.aeterna.ui.theme.ThemeMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val settingsState by viewModel.settingsState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Settings",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Appearance Section
            item {
                SettingsSection(title = "Appearance") {
                    // Theme Selection
                    SettingsCard(
                        title = "Theme",
                        subtitle = "Choose your preferred theme",
                        icon = Icons.Default.Palette
                    ) {
                        Column(modifier = Modifier.selectableGroup()) {
                            ThemeMode.values().forEach { mode ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = settingsState.themeMode == mode,
                                        onClick = { viewModel.updateThemeMode(mode) }
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = mode.name.replace("_", " ").lowercase()
                                            .replaceFirstChar { it.uppercase() },
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                    
                    // Dynamic Color
                    SettingsItem(
                        title = \"Material You\",
                        subtitle = \"Use system accent color\",
                        icon = Icons.Default.Brightness4,
                        trailing = {
                            Switch(
                                checked = settingsState.dynamicColor,
                                onCheckedChange = viewModel::updateDynamicColor
                            )
                        }
                    )
                }
            }
            
            // Audio Section
            item {
                SettingsSection(title = \"Audio\") {
                    // Audio Quality
                    SettingsItem(
                        title = \"High Quality Audio\",
                        subtitle = \"Stream at maximum quality\",
                        icon = Icons.Default.VolumeUp,
                        trailing = {
                            Switch(
                                checked = settingsState.highQualityAudio,
                                onCheckedChange = viewModel::updateHighQualityAudio
                            )
                        }
                    )
                    
                    // Audio Normalization
                    SettingsItem(
                        title = \"Audio Normalization\",
                        subtitle = \"Consistent volume across tracks\",
                        icon = Icons.Default.Equalizer,
                        trailing = {
                            Switch(
                                checked = settingsState.audioNormalization,
                                onCheckedChange = viewModel::updateAudioNormalization
                            )
                        }
                    )
                    
                    // Skip Silence
                    SettingsItem(
                        title = \"Skip Silence\",
                        subtitle = \"Automatically skip silent parts\",
                        icon = Icons.Default.MusicNote,
                        trailing = {
                            Switch(
                                checked = settingsState.skipSilence,
                                onCheckedChange = viewModel::updateSkipSilence
                            )
                        }
                    )
                    
                    // Crossfade Duration
                    SettingsCard(
                        title = \"Crossfade\",
                        subtitle = \"Smooth transitions between songs\",
                        icon = Icons.Default.MusicNote
                    ) {
                        var crossfadeDuration by remember { 
                            mutableFloatStateOf(settingsState.crossfadeDuration) 
                        }
                        
                        Column {
                            Text(
                                text = \"Duration: ${crossfadeDuration.toInt()}s\",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            
                            Slider(
                                value = crossfadeDuration,
                                onValueChange = { crossfadeDuration = it },
                                onValueChangeFinished = {
                                    viewModel.updateCrossfadeDuration(crossfadeDuration)
                                },
                                valueRange = 0f..10f,
                                steps = 9
                            )
                        }
                    }
                }
            }
            
            // Downloads Section
            item {
                SettingsSection(title = \"Downloads\") {
                    // Download Quality
                    SettingsItem(
                        title = \"Download Quality\",
                        subtitle = \"High (320kbps)\",
                        icon = Icons.Default.CloudDownload
                    )
                    
                    // Download on WiFi only
                    SettingsItem(
                        title = \"WiFi Only Downloads\",
                        subtitle = \"Save mobile data\",
                        icon = Icons.Default.CloudDownload,
                        trailing = {
                            Switch(
                                checked = settingsState.wifiOnlyDownloads,
                                onCheckedChange = viewModel::updateWifiOnlyDownloads
                            )
                        }
                    )
                    
                    // Storage Usage
                    SettingsItem(
                        title = \"Storage\",
                        subtitle = \"${settingsState.cacheSize} MB used\",
                        icon = Icons.Default.Storage
                    )
                }
            }
            
            // Privacy & Security
            item {
                SettingsSection(title = \"Privacy & Security\") {
                    SettingsItem(
                        title = \"Private Session\",
                        subtitle = \"Don't save listening history\",
                        icon = Icons.Default.Lock,
                        trailing = {
                            Switch(
                                checked = settingsState.privateSession,
                                onCheckedChange = viewModel::updatePrivateSession
                            )
                        }
                    )
                    
                    SettingsItem(
                        title = \"Analytics\",
                        subtitle = \"Help improve the app\",
                        icon = Icons.Default.Security,
                        trailing = {
                            Switch(
                                checked = settingsState.analytics,
                                onCheckedChange = viewModel::updateAnalytics
                            )
                        }
                    )
                }
            }
            
            // About Section
            item {
                SettingsSection(title = \"About\") {
                    SettingsItem(
                        title = \"Version\",
                        subtitle = \"1.0.0 (Beta)\",
                        icon = Icons.Default.Info
                    )
                    
                    SettingsItem(
                        title = \"Open Source Licenses\",
                        subtitle = \"View third-party licenses\",
                        icon = Icons.Default.Language
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        content()
    }
}

@Composable
fun SettingsCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
fun SettingsItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        onClick = onClick ?: {}
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            trailing?.invoke()
        }
    }
}"
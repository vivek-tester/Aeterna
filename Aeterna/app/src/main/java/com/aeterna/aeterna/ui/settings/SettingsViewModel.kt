package com.aeterna.aeterna.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aeterna.aeterna.ui.theme.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SettingsState(
        val themeMode: ThemeMode = ThemeMode.SYSTEM,
        val dynamicColor: Boolean = true,
        val highQualityAudio: Boolean = true,
        val audioNormalization: Boolean = true,
        val skipSilence: Boolean = false,
        val crossfadeDuration: Float = 3f,
        val wifiOnlyDownloads: Boolean = true,
        val cacheSize: Long = 0L,
        val privateSession: Boolean = false,
        val analytics: Boolean = true
)

@HiltViewModel
class SettingsViewModel
@Inject
constructor(
// Inject data store or repository for persistence
) : ViewModel() {

    private val _settingsState = MutableStateFlow(SettingsState())
    val settingsState: StateFlow<SettingsState> = _settingsState.asStateFlow()

    fun updateThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            _settingsState.value = _settingsState.value.copy(themeMode = mode)
            // Save to persistent storage
        }
    }

    fun updateDynamicColor(enabled: Boolean) {
        viewModelScope.launch {
            _settingsState.value = _settingsState.value.copy(dynamicColor = enabled)
        }
    }

    fun updateHighQualityAudio(enabled: Boolean) {
        viewModelScope.launch {
            _settingsState.value = _settingsState.value.copy(highQualityAudio = enabled)
        }
    }

    fun updateAudioNormalization(enabled: Boolean) {
        viewModelScope.launch {
            _settingsState.value = _settingsState.value.copy(audioNormalization = enabled)
        }
    }

    fun updateSkipSilence(enabled: Boolean) {
        viewModelScope.launch {
            _settingsState.value = _settingsState.value.copy(skipSilence = enabled)
        }
    }

    fun updateCrossfadeDuration(duration: Float) {
        viewModelScope.launch {
            _settingsState.value = _settingsState.value.copy(crossfadeDuration = duration)
        }
    }

    fun updateWifiOnlyDownloads(enabled: Boolean) {
        viewModelScope.launch {
            _settingsState.value = _settingsState.value.copy(wifiOnlyDownloads = enabled)
        }
    }

    fun updatePrivateSession(enabled: Boolean) {
        viewModelScope.launch {
            _settingsState.value = _settingsState.value.copy(privateSession = enabled)
        }
    }

    fun updateAnalytics(enabled: Boolean) {
        viewModelScope.launch {
            _settingsState.value = _settingsState.value.copy(analytics = enabled)
        }
    }
}

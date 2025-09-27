package com.aeterna.aeterna.ui.theme

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {
    
    private val THEME_MODE_KEY = stringPreferencesKey(\"theme_mode\")
    private val DYNAMIC_COLOR_KEY = booleanPreferencesKey(\"dynamic_color\")
    private val THEME_CONTRAST_KEY = stringPreferencesKey(\"theme_contrast\")
    private val MONOCHROME_ACCENT_KEY = booleanPreferencesKey(\"monochrome_accent\")
    private val CUSTOM_PRIMARY_COLOR_KEY = longPreferencesKey(\"custom_primary_color\")
    
    val themeConfig: Flow<ThemeConfig> = dataStore.data.map { preferences ->
        ThemeConfig(
            mode = ThemeMode.valueOf(
                preferences[THEME_MODE_KEY] ?: ThemeMode.SYSTEM.name
            ),
            dynamicColor = preferences[DYNAMIC_COLOR_KEY] ?: true,
            contrast = ThemeContrast.valueOf(
                preferences[THEME_CONTRAST_KEY] ?: ThemeContrast.STANDARD.name
            ),
            monochromeAccent = preferences[MONOCHROME_ACCENT_KEY] ?: false,
            customPrimaryColor = preferences[CUSTOM_PRIMARY_COLOR_KEY]
        )
    }
    
    suspend fun updateThemeMode(mode: ThemeMode) {
        dataStore.edit { preferences ->
            preferences[THEME_MODE_KEY] = mode.name
        }
    }
    
    suspend fun updateDynamicColor(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[DYNAMIC_COLOR_KEY] = enabled
        }
    }
    
    suspend fun updateThemeContrast(contrast: ThemeContrast) {
        dataStore.edit { preferences ->
            preferences[THEME_CONTRAST_KEY] = contrast.name
        }
    }
    
    suspend fun updateMonochromeAccent(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[MONOCHROME_ACCENT_KEY] = enabled
        }
    }
    
    suspend fun updateCustomPrimaryColor(color: Long?) {
        dataStore.edit { preferences ->
            if (color != null) {
                preferences[CUSTOM_PRIMARY_COLOR_KEY] = color
            } else {
                preferences.remove(CUSTOM_PRIMARY_COLOR_KEY)
            }
        }
    }
}
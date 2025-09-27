package com.aeterna.core.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/** DataStore for accessibility preferences */
@Singleton
class AccessibilityPreferencesDataStore
@Inject
constructor(@ApplicationContext private val context: Context) {

    private val Context.accessibilityDataStore: DataStore<Preferences> by
            preferencesDataStore(name = "accessibility_preferences")

    companion object {
        private val HIGH_CONTRAST_MODE = booleanPreferencesKey("high_contrast_mode")
        private val REDUCE_MOTION = booleanPreferencesKey("reduce_motion")
        private val LARGE_TEXT = booleanPreferencesKey("large_text")
        private val SCREEN_READER_OPTIMIZED = booleanPreferencesKey("screen_reader_optimized")
        private val HAPTIC_FEEDBACK = booleanPreferencesKey("haptic_feedback")
    }

    val highContrastMode: Flow<Boolean> =
            context.accessibilityDataStore.data.map { preferences ->
                preferences[HIGH_CONTRAST_MODE] ?: false
            }

    val reduceMotion: Flow<Boolean> =
            context.accessibilityDataStore.data.map { preferences ->
                preferences[REDUCE_MOTION] ?: false
            }

    val largeText: Flow<Boolean> =
            context.accessibilityDataStore.data.map { preferences ->
                preferences[LARGE_TEXT] ?: false
            }

    val screenReaderOptimized: Flow<Boolean> =
            context.accessibilityDataStore.data.map { preferences ->
                preferences[SCREEN_READER_OPTIMIZED] ?: false
            }

    val hapticFeedback: Flow<Boolean> =
            context.accessibilityDataStore.data.map { preferences ->
                preferences[HAPTIC_FEEDBACK] ?: true
            }

    suspend fun setHighContrastMode(enabled: Boolean) {
        context.accessibilityDataStore.edit { preferences ->
            preferences[HIGH_CONTRAST_MODE] = enabled
        }
    }

    suspend fun setReduceMotion(enabled: Boolean) {
        context.accessibilityDataStore.edit { preferences -> preferences[REDUCE_MOTION] = enabled }
    }

    suspend fun setLargeText(enabled: Boolean) {
        context.accessibilityDataStore.edit { preferences -> preferences[LARGE_TEXT] = enabled }
    }

    suspend fun setScreenReaderOptimized(enabled: Boolean) {
        context.accessibilityDataStore.edit { preferences ->
            preferences[SCREEN_READER_OPTIMIZED] = enabled
        }
    }

    suspend fun setHapticFeedback(enabled: Boolean) {
        context.accessibilityDataStore.edit { preferences ->
            preferences[HAPTIC_FEEDBACK] = enabled
        }
    }
}

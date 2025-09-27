package com.aeterna.core.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import net.openid.appauth.AuthState

private val Context.authDataStore by preferencesDataStore(name = "auth_preferences")

@Singleton
class AuthDataStore @Inject constructor(@ApplicationContext private val context: Context) {

    private val AUTH_STATE_KEY = stringPreferencesKey("auth_state")

    fun getAuthStateFlow() =
            context.authDataStore.data.map { preferences ->
                preferences[AUTH_STATE_KEY]?.let { AuthState.jsonDeserialize(it) }
            }

    suspend fun saveAuthState(authState: AuthState) {
        context.authDataStore.edit { preferences ->
            preferences[AUTH_STATE_KEY] = authState.jsonSerializeString()
        }
    }

    suspend fun getAuthState(): AuthState? {
        return getAuthStateFlow().first()
    }

    suspend fun clearAuthState() {
        context.authDataStore.edit { preferences -> preferences.remove(AUTH_STATE_KEY) }
    }
}

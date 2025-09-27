package com.aeterna.core.data

import android.content.Intent
import com.aeterna.core.data.datastore.AuthDataStore
import com.aeterna.core.domain.AuthRepository
import com.aeterna.core.youtube.AuthManager
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationResponse
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authManager: AuthManager,
    private val authDataStore: AuthDataStore
) : AuthRepository {

    override fun getAuthRequestIntent(): Intent {
        return authManager.getAuthRequestIntent()
    }

    override suspend fun exchangeToken(authorizationResponse: AuthorizationResponse): AuthState? {
        return suspendCoroutine { continuation ->
            authManager.exchangeToken(authorizationResponse) { authState ->
                if (authState != null) {
                    continuation.resume(authState)
                } else {
                    continuation.resume(null)
                }
            }
        }
    }

    override suspend fun getAuthState(): AuthState? {
        return authDataStore.getAuthState()
    }

    override suspend fun saveAuthState(authState: AuthState) {
        authDataStore.saveAuthState(authState)
    }

    override suspend fun clearAuthState() {
        authDataStore.clearAuthState()
    }
}
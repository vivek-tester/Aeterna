package com.aeterna.core.domain

import android.content.Intent
import net.openid.appauth.AuthState

interface AuthRepository {
    fun getAuthRequestIntent(): Intent
    suspend fun exchangeToken(authorizationResponse: net.openid.appauth.AuthorizationResponse): AuthState?
    suspend fun getAuthState(): AuthState?
    suspend fun saveAuthState(authState: AuthState)
    suspend fun clearAuthState()
}
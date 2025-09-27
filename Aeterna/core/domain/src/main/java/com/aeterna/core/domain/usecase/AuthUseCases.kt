package com.aeterna.core.domain.usecase

import android.content.Intent
import com.aeterna.core.domain.AuthRepository
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationResponse
import javax.inject.Inject

class GetAuthRequestIntent @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(): Intent {
        return repository.getAuthRequestIntent()
    }
}

class ExchangeToken @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(authorizationResponse: AuthorizationResponse): AuthState? {
        return repository.exchangeToken(authorizationResponse)
    }
}

class GetAuthState @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): AuthState? {
        return repository.getAuthState()
    }
}

class SaveAuthState @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(authState: AuthState) {
        repository.saveAuthState(authState)
    }
}

class ClearAuthState @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke() {
        repository.clearAuthState()
    }
}

data class AuthUseCases @Inject constructor(
    val getAuthRequestIntent: GetAuthRequestIntent,
    val exchangeToken: ExchangeToken,
    val getAuthState: GetAuthState,
    val saveAuthState: SaveAuthState,
    val clearAuthState: ClearAuthState
)
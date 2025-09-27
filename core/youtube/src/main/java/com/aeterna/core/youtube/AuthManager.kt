package com.aeterna.core.youtube

import android.content.Context
import android.content.Intent
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val serviceConfiguration = AuthorizationServiceConfiguration(
        Uri.parse(AuthConstants.AUTHORIZATION_URL),
        Uri.parse(AuthConstants.TOKEN_URL)
    )

    fun getAuthRequestIntent(): Intent {
        val authRequest = AuthorizationRequest.Builder(
            serviceConfiguration,
            AuthConstants.CLIENT_ID,
            ResponseTypeValues.CODE,
            Uri.parse(AuthConstants.REDIRECT_URI)
        ).setScope(AuthConstants.SCOPE)
            .build()

        val authService = AuthorizationService(context)
        return authService.getAuthorizationRequestIntent(authRequest)
    }

    fun exchangeToken(authorizationResponse: net.openid.appauth.AuthorizationResponse, callback: (AuthState?) -> Unit) {
        val authService = AuthorizationService(context)
        authService.performTokenRequest(
            authorizationResponse.createTokenExchangeRequest(),
            net.openid.appauth.ClientSecretPost(AuthConstants.CLIENT_SECRET)
        ) { response, ex ->
            if (response != null) {
                // AuthState does not have a constructor that accepts TokenResponse in this AppAuth version.
                // Create an empty AuthState and update it with the token response.
                val authState = AuthState()
                authState.update(response, ex)
                callback(authState)
            } else {
                callback(null)
            }
        }
    }
}
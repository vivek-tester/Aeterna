package com.aeterna.core.youtube

object AuthConstants {
    const val CLIENT_ID = "dummy_client_id"
    const val CLIENT_SECRET = "dummy_client_secret"
    const val REDIRECT_URI = "com.aeterna.aeterna:/oauth2redirect"
    const val AUTHORIZATION_URL = "https://accounts.google.com/o/oauth2/v2/auth"
    const val TOKEN_URL = "https://oauth2.googleapis.com/token"
    const val SCOPE = "https://www.googleapis.com/auth/youtube" // Broader scope for YouTube access
    const val YTM_BASE_URL = "https://music.youtube.com/" // Base URL for YouTube Music API
}
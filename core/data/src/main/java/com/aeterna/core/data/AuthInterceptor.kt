package com.aeterna.core.data

import com.aeterna.core.data.datastore.AuthDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val authDataStore: AuthDataStore
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        runBlocking {
            val authState = authDataStore.getAuthStateFlow().first()
            authState?.accessToken?.let { accessToken ->
                requestBuilder.addHeader("Authorization", "Bearer $accessToken")
            }
        }

        return chain.proceed(requestBuilder.build())
    }
}
package com.aeterna.core.domain.usecase

import com.aeterna.core.domain.AuthRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AuthUseCasesTest {

    private val mockAuthRepository = mockk<AuthRepository>()
    private lateinit var getAuthState: GetAuthState
    private lateinit var saveAuthState: SaveAuthState
    private lateinit var clearAuthState: ClearAuthState

    @Before
    fun setup() {
        getAuthState = GetAuthState(mockAuthRepository)
        saveAuthState = SaveAuthState(mockAuthRepository)
        clearAuthState = ClearAuthState(mockAuthRepository)
    }

    @Test
    fun `getAuthState returns auth state from repository`() = runTest {
        // Given
        val expectedAuthState = mockk<net.openid.appauth.AuthState>()
        coEvery { mockAuthRepository.getAuthState() } returns expectedAuthState

        // When
        val result = getAuthState()

        // Then
        assertEquals(expectedAuthState, result)
    }

    @Test
    fun `saveAuthState calls repository save method`() = runTest {
        // Given
        val authState = mockk<net.openid.appauth.AuthState>()
        coEvery { mockAuthRepository.saveAuthState(authState) } returns Unit

        // When
        saveAuthState(authState)

        // Then
        io.mockk.verify { runTest { mockAuthRepository.saveAuthState(authState) } }
    }

    @Test
    fun `clearAuthState calls repository clear method`() = runTest {
        // Given
        coEvery { mockAuthRepository.clearAuthState() } returns Unit

        // When
        clearAuthState()

        // Then
        io.mockk.verify { runTest { mockAuthRepository.clearAuthState() } }
    }
}

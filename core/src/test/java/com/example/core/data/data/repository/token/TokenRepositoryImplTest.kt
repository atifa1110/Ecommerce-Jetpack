package com.example.core.data.data.repository.token

import com.example.core.data.local.datastore.AuthTokenDataStore
import com.example.core.data.repository.token.TokenRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class TokenRepositoryImplTest {

    private lateinit var authTokenDataStore: AuthTokenDataStore
    private lateinit var repository: TokenRepositoryImpl

    @Before
    fun setUp() {
        authTokenDataStore = mockk(relaxed = true)
        repository = TokenRepositoryImpl(authTokenDataStore)
    }

    @Test
    fun `setToken calls saveAccessToken`() = runTest {
        val token = "access_token"
        repository.setToken(token)
        coVerify { authTokenDataStore.saveAccessToken(token) }
    }

    @Test
    fun `setRefresh calls saveRefreshToken`() = runTest {
        val token = "refresh_token"
        repository.setRefresh(token)
        coVerify { authTokenDataStore.saveRefreshToken(token) }
    }

    @Test
    fun `getToken returns access token`() = runTest {
        val expectedToken = "access_token"
        coEvery { authTokenDataStore.getAccessToken() } returns flowOf(expectedToken)

        val result = repository.getToken()
        assertEquals(expectedToken, result)
    }

    @Test
    fun `getRefresh returns refresh token`() = runTest {
        val expectedToken = "refresh_token"
        coEvery { authTokenDataStore.getRefreshToken() } returns flowOf(expectedToken)

        val result = repository.getRefresh()
        assertEquals(expectedToken, result)
    }

    @Test
    fun `clearAuthToken calls clearAuthTokens`() = runTest {
        repository.clearAuthToken()
        coVerify { authTokenDataStore.clearAuthTokens() }
    }
}
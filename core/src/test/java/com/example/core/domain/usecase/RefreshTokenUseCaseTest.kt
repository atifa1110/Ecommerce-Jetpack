package com.example.core.domain.usecase

import app.cash.turbine.test
import com.example.core.data.network.request.TokenRequest
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.domain.repository.auth.AuthRepository
import com.example.core.domain.repository.token.TokenRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class RefreshTokenUseCaseTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var tokenRepository: TokenRepository
    private lateinit var useCase: RefreshTokenUseCase

    @Before
    fun setUp() {
        authRepository = mockk()
        tokenRepository = mockk()
        useCase = RefreshTokenUseCase(authRepository, tokenRepository)
    }

    @Test
    fun `invoke returns Success when refresh token exists`() = runTest {
        // Given
        val dummyRefreshToken = "token"
        val expectedResponse = EcommerceResponse.Success("success")

        coEvery { tokenRepository.getRefresh() } returns dummyRefreshToken
        coEvery { authRepository.refresh(TokenRequest(dummyRefreshToken)) } returns flowOf(expectedResponse)

        // When
        val result = useCase()

        // Then
        result.test {
            val success = awaitItem()
            assertEquals(expectedResponse, success)
            awaitComplete()
        }

        coVerify {  tokenRepository.getRefresh()  }
        coVerify { authRepository.refresh(TokenRequest(dummyRefreshToken)) }
    }

    @Test
    fun `invoke returns Failure when refresh token is null`() = runTest {
        // Given
        coEvery { tokenRepository.getRefresh() } returns null

        // When
        val result = useCase()

        // Then
        result.test {
            val item = awaitItem()
            assertEquals(EcommerceResponse.Failure(-1, "No refresh token found"), item)
            awaitComplete()
        }
    }
}

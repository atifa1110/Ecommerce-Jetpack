package com.example.core.domain.usecase

import com.example.core.domain.repository.token.TokenRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ClearAuthTokenUseCaseTest {

    private lateinit var repository: TokenRepository
    private lateinit var useCase: ClearAuthTokenUseCase

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        useCase = ClearAuthTokenUseCase(repository)
    }

    @Test
    fun `invoke should call clearAuthToken`() = runTest {
        // Given
        coEvery { repository.clearAuthToken() } returns Unit

        // When
        useCase()

        // Then
        coVerify(exactly = 1) { repository.clearAuthToken() }
    }
}

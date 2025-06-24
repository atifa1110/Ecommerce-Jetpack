package com.example.core.domain.usecase

import app.cash.turbine.test
import com.example.core.domain.repository.messaging.MessagingRepository
import com.example.core.data.network.response.EcommerceResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class UpdateFcmTokenUseCaseTest {

    private lateinit var repository: MessagingRepository
    private lateinit var useCase: UpdateFcmTokenUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = UpdateFcmTokenUseCase(repository)
    }

    @Test
    fun `invoke should emit success response`() = runTest {
        // Given
        val expected = EcommerceResponse.Success("TokenUpdated")
        coEvery { repository.updateFcmToken() } returns flowOf(expected)

        // When
        val result = useCase()

        // Then
        result.test {
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }
}

package com.example.core.domain.usecase

import app.cash.turbine.test
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.domain.repository.auth.AuthRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class ProfileUseCaseTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var useCase: ProfileUseCase

    @Before
    fun setUp() {
        authRepository = mockk()
        useCase = ProfileUseCase(authRepository)
    }

    @Test
    fun `invoke should emit Success response`() = runTest {
        // Given
        val dummyImage = mockk<MultipartBody.Part>()
        val dummyName = mockk<MultipartBody.Part>()
        val expectedResponse = EcommerceResponse.Success("Profile updated")

        coEvery {
            authRepository.profile(dummyImage, dummyName)
        } returns flowOf(expectedResponse)

        // When
        val result = useCase(dummyImage, dummyName)

        // Then
        result.test {
            val emitted = awaitItem()
            assertEquals(expectedResponse, emitted)
            awaitComplete()
        }
    }

    @Test
    fun `invoke should emit Failure response`() = runTest {
        // Given
        val dummyImage = mockk<MultipartBody.Part>()
        val dummyName = mockk<MultipartBody.Part>()
        val expectedResponse = EcommerceResponse.Failure(400, "Bad Request")

        coEvery {
            authRepository.profile(dummyImage, dummyName)
        } returns flowOf(expectedResponse)

        // When
        val result = useCase(dummyImage, dummyName)

        // Then
        result.test {
            val emitted = awaitItem()
            assertEquals(expectedResponse, emitted)
            awaitComplete()
        }
    }
}

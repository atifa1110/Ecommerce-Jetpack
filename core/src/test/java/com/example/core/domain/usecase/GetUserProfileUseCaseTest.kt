package com.example.core.domain.usecase

import app.cash.turbine.test
import com.example.core.domain.repository.user.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class GetUserProfileUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var useCase: GetUserProfileUseCase

    @Before
    fun setUp() {
        userRepository = mockk()
        useCase = GetUserProfileUseCase(userRepository)
    }

    @Test
    fun `invoke should return user image URL from repository`() = runTest {
        // Given
        val expectedImageUrl = "https://example.com/profile.jpg"
        coEvery { userRepository.getUserImage() } returns flowOf(expectedImageUrl)

        // When & Then
        useCase().test {
            val result = awaitItem()
            assertEquals(expectedImageUrl, result)
            awaitComplete()
        }
    }
}

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

class GetUserNameUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var useCase: GetUserNameUseCase

    @Before
    fun setUp() {
        userRepository = mockk()
        useCase = GetUserNameUseCase(userRepository)
    }

    @Test
    fun `invoke should return user name from repository`() = runTest {
        // Given
        val expectedName = "Atifa"
        coEvery { userRepository.getUserName() } returns flowOf(expectedName)

        // When & Then
        useCase().test {
            val result = awaitItem()
            assertEquals(expectedName, result)
            awaitComplete()
        }
    }
}

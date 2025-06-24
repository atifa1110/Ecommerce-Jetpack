package com.example.core.domain.usecase

import app.cash.turbine.test
import com.example.core.domain.repository.state.StateRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class GetProfileStateUseCaseTest {

    private lateinit var repository: StateRepository
    private lateinit var useCase: GetProfileStateUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetProfileStateUseCase(repository)
    }

    @Test
    fun `invoke should return correct profile state`() = runTest {
        // Arrange
        every { repository.getProfile() } returns flowOf(true)

        // Act & Assert
        useCase().test {
            assertEquals(true, awaitItem())
            awaitComplete()
        }
    }
}

package com.example.core.domain.usecase

import app.cash.turbine.test
import com.example.core.domain.repository.state.StateRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class GetDarkStateUseCaseTest {

    private lateinit var repository: StateRepository
    private lateinit var useCase: GetDarkStateUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetDarkStateUseCase(repository)
    }

    @Test
    fun `invoke should emit current dark mode state`() = runTest {
        // Given
        val expected = true
        coEvery { repository.getDarkMode() } returns flowOf(expected)

        // When & Then
        useCase().test {
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }
}

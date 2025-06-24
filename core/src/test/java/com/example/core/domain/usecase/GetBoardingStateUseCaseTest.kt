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

class GetBoardingStateUseCaseTest {

    private lateinit var stateRepository: StateRepository
    private lateinit var useCase: GetBoardingStateUseCase

    @Before
    fun setUp() {
        stateRepository = mockk()
        useCase = GetBoardingStateUseCase(stateRepository)
    }

    @Test
    fun `invoke should emit correct onboarding state`() = runTest {
        // Given
        val expectedState = true
        coEvery { stateRepository.getBoarding() } returns flowOf(expectedState)

        // When & Then
        useCase().test {
            assertEquals(expectedState, awaitItem())
            awaitComplete()
        }
    }
}

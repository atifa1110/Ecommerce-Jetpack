package com.example.core.domain.usecase

import com.example.core.domain.repository.state.StateRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SetBoardingStateUseCaseTest {

    private lateinit var stateRepository: StateRepository
    private lateinit var useCase: SetBoardingStateUseCase

    @Before
    fun setUp() {
        stateRepository = mockk(relaxed = true)
        useCase = SetBoardingStateUseCase(stateRepository)
    }

    @Test
    fun `invoke should call setBoarding with true`() = runTest {
        // When
        useCase(true)

        // Then
        coVerify(exactly = 1) { stateRepository.setBoarding(true) }
    }

    @Test
    fun `invoke should call setBoarding with false`() = runTest {
        // When
        useCase(false)

        // Then
        coVerify(exactly = 1) { stateRepository.setBoarding(false) }
    }
}

package com.example.core.domain.usecase

import com.example.core.domain.repository.state.StateRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SetDarkStateUseCaseTest {

    private lateinit var stateRepository: StateRepository
    private lateinit var useCase: SetDarkStateUseCase

    @Before
    fun setUp() {
        stateRepository = mockk(relaxed = true)
        useCase = SetDarkStateUseCase(stateRepository)
    }

    @Test
    fun `invoke should call setDarkMode with true`() = runTest {
        // When
        useCase(true)

        // Then
        coVerify(exactly = 1) { stateRepository.setDarkMode(true) }
    }

    @Test
    fun `invoke should call setDarkMode with false`() = runTest {
        // When
        useCase(false)

        // Then
        coVerify(exactly = 1) { stateRepository.setDarkMode(false) }
    }
}

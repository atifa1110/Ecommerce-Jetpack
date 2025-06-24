package com.example.core.domain.usecase

import com.example.core.domain.repository.state.StateRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ClearAppStateUseCaseTest {

    private lateinit var repository: StateRepository
    private lateinit var useCase: ClearAppStateUseCase

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        useCase = ClearAppStateUseCase(repository)
    }

    @Test
    fun `invoke should call clearAppState`() = runTest {
        // Given
        coEvery { repository.clearAppState() } returns Unit

        // When
        useCase()

        // Then
        coVerify(exactly = 1) { repository.clearAppState() }
    }
}

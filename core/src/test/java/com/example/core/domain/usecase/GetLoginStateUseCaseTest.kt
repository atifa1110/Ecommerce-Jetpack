package com.example.core.domain.usecase

import app.cash.turbine.test
import com.example.core.domain.repository.state.StateRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetLoginStateUseCaseTest {

    private lateinit var repository: StateRepository
    private lateinit var useCase: GetLoginStateUseCase

    @BeforeTest
    fun setUp() {
        repository = mockk()
        useCase = GetLoginStateUseCase(repository)
    }

    @Test
    fun `invoke should emit login state true`() = runTest {
        // Given
        coEvery { repository.getLogin() } returns flowOf(true)

        // When & Then
        useCase().test {
            assertEquals(true, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `invoke should emit login state false`() = runTest {
        // Given
        coEvery { repository.getLogin() } returns flowOf(false)

        // When & Then
        useCase().test {
            assertEquals(false, awaitItem())
            awaitComplete()
        }
    }
}

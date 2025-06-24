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

class GetLanguageCodeStateUseCaseTest {

    private lateinit var repository: StateRepository
    private lateinit var useCase: GetLanguageCodeStateUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetLanguageCodeStateUseCase(repository)
    }

    @Test
    fun `invoke should emit current language code`() = runTest {
        // Given
        val expectedCode = "id"
        coEvery { repository.getLanguageCode() } returns flowOf(expectedCode)

        // When & Then
        useCase().test {
            assertEquals(expectedCode, awaitItem())
            awaitComplete()
        }
    }
}

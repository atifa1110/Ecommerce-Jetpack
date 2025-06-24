package com.example.core.domain.usecase

import com.example.core.domain.repository.user.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ClearUserDataUseCaseTest {

    private lateinit var repository: UserRepository
    private lateinit var useCase: ClearUserDataUseCase

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        useCase = ClearUserDataUseCase(repository)
    }

    @Test
    fun `invoke should call clearUserData on repository`() = runTest {
        // Given
        coEvery { repository.clearUserData() } returns Unit

        // When
        useCase()

        // Then
        coVerify(exactly = 1) { repository.clearUserData() }
    }
}

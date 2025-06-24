package com.example.core.domain.usecase

import com.example.core.data.local.datastore.AppStateDataStore
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SetLanguageCodeUseCaseTest {

    private lateinit var dataStore: AppStateDataStore
    private lateinit var useCase: SetLanguageCodeUseCase

    @Before
    fun setUp() {
        dataStore = mockk(relaxed = true)
        useCase = SetLanguageCodeUseCase(dataStore)
    }

    @Test
    fun `invoke should call setLanguageCode with correct value`() = runTest {
        // Given
        val expectedCode = "id"

        // When
        useCase(expectedCode)

        // Then
        coVerify(exactly = 1) { dataStore.setLanguageCode(expectedCode) }
    }
}

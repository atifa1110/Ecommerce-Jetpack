package com.example.core.domain.usecase

import com.example.core.domain.repository.notification.NotificationRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetNotificationSizeUseCaseTest {

    private lateinit var repository: NotificationRepository
    private lateinit var useCase: GetNotificationSizeUseCase

    @BeforeTest
    fun setUp() {
        repository = mockk()
        useCase = GetNotificationSizeUseCase(repository)
    }

    @Test
    fun `invoke should return unread notification count`() = runTest {
        // Given
        val expectedCount = 5
        coEvery { repository.getUnreadNotification() } returns expectedCount

        // When
        val result = useCase()

        // Then
        assertEquals(expectedCount, result)
    }
}

package com.example.core.domain.usecase

import com.example.core.domain.repository.notification.NotificationRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.just
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpdateNotificationUseCaseTest {

    private lateinit var repository: NotificationRepository
    private lateinit var useCase: UpdateNotificationUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = UpdateNotificationUseCase(repository)
    }

    @Test
    fun `invoke should call repository to update notification`() = runTest {
        // Given
        val notificationId = 123
        val read = true
        // Stub the repository method to do nothing (just runs)
        coEvery { repository.updateReadNotification(notificationId, read) } just runs

        // When
        useCase(notificationId, read)

        // Then
        coVerify(exactly = 1) {
            repository.updateReadNotification(notificationId, read)
        }
    }
}

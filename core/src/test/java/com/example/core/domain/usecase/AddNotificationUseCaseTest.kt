package com.example.core.domain.usecase

import com.example.core.domain.model.NotificationModel
import com.example.core.domain.repository.notification.NotificationRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class


AddNotificationUseCaseTest {

    private lateinit var repository: NotificationRepository
    private lateinit var useCase: AddNotificationUseCase

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        useCase = AddNotificationUseCase(repository)
    }

    @Test
    fun `invoke should call addNotification with correct data`() = runTest {
        // Given
        val notification = NotificationModel(
            id = 1,
            title = "Test",
            body = "body",
            image = "image",
            type = "type",
            date = "date",
            time = "time",
            isRead = false
        )

        coEvery { repository.addNotification(notification) } returns Unit

        // When
        useCase(notification)

        // Then
        coVerify(exactly = 1) { repository.addNotification(notification) }
    }
}

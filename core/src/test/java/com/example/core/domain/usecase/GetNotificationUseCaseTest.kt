package com.example.core.domain.usecase

import app.cash.turbine.test
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.domain.model.NotificationModel
import com.example.core.domain.repository.notification.NotificationRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetNotificationUseCaseTest {

    private lateinit var repository: NotificationRepository
    private lateinit var useCase: GetNotificationUseCase

    @BeforeTest
    fun setUp() {
        repository = mockk()
        useCase = GetNotificationUseCase(repository)
    }

    @Test
    fun `invoke should return notification response`() = runTest {
        // Given
        val notifications = listOf(
            NotificationModel(id = 1,
                title = "Promo Hari Ini!",
                body = "Diskon 50% untuk produk terpilih.",
                image = "https://example.com/image.jpg",
                type = "promo",
                date = "2025-06-16",
                time = "09:00",
                isRead = false),
            NotificationModel(id = 2,
                title = "Promo Hari Ini!",
                body = "Diskon 50% untuk produk terpilih.",
                image = "https://example.com/image.jpg",
                type = "promo",
                date = "2025-06-16",
                time = "09:00",
                isRead = false)
        )
        val expectedResponse = EcommerceResponse.Success(notifications)
        coEvery { repository.getNotification() } returns flowOf(expectedResponse)

        // When & Then
        useCase().test {
            assertEquals(expectedResponse, awaitItem())
            awaitComplete()
        }
    }
}

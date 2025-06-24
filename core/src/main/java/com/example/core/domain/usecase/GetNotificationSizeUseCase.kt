package com.example.core.domain.usecase

import com.example.core.domain.repository.notification.NotificationRepository
import javax.inject.Inject

class GetNotificationSizeUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(): Int {
        return notificationRepository.getUnreadNotification()
    }
}
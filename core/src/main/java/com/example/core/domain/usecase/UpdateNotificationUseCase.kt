package com.example.core.domain.usecase

import com.example.core.domain.repository.notification.NotificationRepository
import javax.inject.Inject

class UpdateNotificationUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(id: Int, read: Boolean) {
        return notificationRepository.updateReadNotification(id, read)
    }
}
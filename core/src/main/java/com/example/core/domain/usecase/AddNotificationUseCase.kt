package com.example.core.domain.usecase

import com.example.core.domain.model.NotificationModel
import com.example.core.domain.repository.notification.NotificationRepository
import javax.inject.Inject

class AddNotificationUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
){
    suspend operator fun invoke(notification: NotificationModel) {
        return notificationRepository.addNotification(notification)
    }
}
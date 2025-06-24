package com.example.core.domain.usecase

import com.example.core.domain.model.NotificationModel
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.domain.repository.notification.NotificationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNotificationUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    operator fun invoke () : Flow<EcommerceResponse<List<NotificationModel>>> {
        return notificationRepository.getNotification()
    }
}
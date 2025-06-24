package com.example.core.domain.repository.notification

import com.example.core.data.network.response.EcommerceResponse
import com.example.core.domain.model.NotificationModel
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    suspend fun addNotification(notification: NotificationModel)
    fun getNotification(): Flow<EcommerceResponse<List<NotificationModel>>>
    suspend fun getUnreadNotification(): Int
    suspend fun updateReadNotification(id: Int, read: Boolean)
}
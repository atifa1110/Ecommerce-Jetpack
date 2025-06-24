package com.example.core.data.repository.notification

import com.example.core.domain.repository.notification.NotificationRepository
import com.example.core.domain.model.NotificationModel
import com.example.core.data.mapper.asNotificationModel
import com.example.core.data.mapper.listMap
import com.example.core.data.local.datasource.NotificationDatabaseSource
import com.example.core.data.local.room.entity.notification.NotificationEntity
import com.example.core.data.network.response.EcommerceResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationDatabaseSource: NotificationDatabaseSource
): NotificationRepository {
    override suspend fun addNotification(notification: NotificationModel) {
        return notificationDatabaseSource.addNotification(notification)
    }

    override fun getNotification(): Flow<EcommerceResponse<List<NotificationModel>>> {
        return flow{
            try {
                emit(EcommerceResponse.Loading)
                delay(1000L)
                val result = notificationDatabaseSource.getNotification().listMap(NotificationEntity::asNotificationModel).first()
                emit(EcommerceResponse.Success(result))
            } catch (e: Exception) {
                emit(EcommerceResponse.Failure(code = -1, error = e.message ?: "Unknown error"))
            }
        }
    }

    override suspend fun getUnreadNotification(): Int {
        return notificationDatabaseSource.getUnreadNotification()
    }

    override suspend fun updateReadNotification(id: Int, read: Boolean) {
        return notificationDatabaseSource.updateReadNotification(id,read)
    }
}
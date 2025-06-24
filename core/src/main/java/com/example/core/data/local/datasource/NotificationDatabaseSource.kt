package com.example.core.data.local.datasource

import com.example.core.data.local.room.dao.notification.NotificationDao
import com.example.core.data.mapper.asNotificationEntity
import com.example.core.domain.model.NotificationModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class NotificationDatabaseSource @Inject constructor(
    private val notificationDao: NotificationDao
) {
    fun getNotification() = notificationDao.getAllNotification()
    fun addNotification(notification: NotificationModel) =
        notificationDao.addNotification(notification.asNotificationEntity())

    suspend fun getUnreadNotification(): Int {
        val data = notificationDao.getNotificationRead(false).first()
        return if (data.isNotEmpty()) data.size else 0
    }

    suspend fun updateReadNotification(id: Int, read: Boolean) =
        notificationDao.updateReadNotification(id, read)
}
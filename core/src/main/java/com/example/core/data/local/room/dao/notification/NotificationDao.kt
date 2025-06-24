package com.example.core.data.local.room.dao.notification

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.core.data.local.room.entity.notification.NotificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNotification(notification: NotificationEntity)

    @Query("SELECT * FROM notification")
    fun getAllNotification(): Flow<List<NotificationEntity>>

    @Query("SELECT * FROM notification WHERE isRead = :read")
    fun getNotificationRead(read: Boolean): Flow<List<NotificationEntity>>

    @Query("UPDATE notification SET isRead = :read WHERE id = :id")
    suspend fun updateReadNotification(id: Int, read: Boolean)

}
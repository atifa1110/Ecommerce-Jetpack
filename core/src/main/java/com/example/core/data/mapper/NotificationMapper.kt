package com.example.core.data.mapper

import com.example.core.domain.model.NotificationModel
import com.example.core.data.local.room.entity.notification.NotificationEntity

fun NotificationEntity.asNotificationModel() = NotificationModel(
    id = id,
    title = title,
    body = body,
    image = image,
    type = type,
    date = date,
    time = time,
    isRead = isRead
)

fun NotificationModel.asNotificationEntity() = NotificationEntity(
    id = id,
    title = title,
    body = body,
    image = image,
    type = type,
    date = date,
    time = time,
    isRead = isRead
)
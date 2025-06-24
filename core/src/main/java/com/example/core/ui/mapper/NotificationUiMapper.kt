package com.example.core.ui.mapper

import com.example.core.domain.model.NotificationModel
import com.example.core.ui.model.Notification

fun List<NotificationModel>.asNotification() = map{it.asNotification()}
fun NotificationModel.asNotification() = Notification(
    id = id,
    title = title,
    body = body,
    image = image,
    type = type,
    date = date,
    time = time,
    isRead = isRead
)
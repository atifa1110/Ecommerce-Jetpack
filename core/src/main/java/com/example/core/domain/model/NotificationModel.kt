package com.example.core.domain.model

data class NotificationModel (
    val id : Int? = null,
    val title: String,
    val body: String,
    val image: String,
    val type: String,
    val date: String,
    val time: String,
    val isRead : Boolean
)
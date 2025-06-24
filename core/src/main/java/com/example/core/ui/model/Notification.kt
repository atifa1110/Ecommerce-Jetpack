package com.example.core.ui.model

data class Notification (
    val id : Int? = null,
    val title: String,
    val body: String,
    val image: String,
    val type: String,
    val date: String,
    val time: String,
    val isRead : Boolean
)
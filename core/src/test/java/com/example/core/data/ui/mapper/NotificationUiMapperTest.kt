package com.example.core.data.ui.mapper

import com.example.core.ui.mapper.asNotification
import com.example.core.domain.model.NotificationModel
import kotlin.test.Test
import kotlin.test.assertEquals

class NotificationUiMapperTest {

    @Test
    fun `NotificationModel should map to Notification correctly`() {
        val model = NotificationModel(
            id = 1,
            title = "Order Update",
            body = "Your order has been shipped.",
            image = "image_url",
            type = "shipping",
            date = "2025-06-16",
            time = "11:00",
            isRead = false
        )

        val result = model.asNotification()

        assertEquals(model.id, result.id)
        assertEquals(model.title, result.title)
        assertEquals(model.body, result.body)
        assertEquals(model.image, result.image)
        assertEquals(model.type, result.type)
        assertEquals(model.date, result.date)
        assertEquals(model.time, result.time)
        assertEquals(model.isRead, result.isRead)
    }

    @Test
    fun `List of NotificationModel should map to List of Notification`() {
        val models = listOf(
            NotificationModel(1, "A", "Body A", "imgA", "typeA", "2025-06-16", "10:00", false),
            NotificationModel(2, "B", "Body B", "imgB", "typeB", "2025-06-17", "11:00", true)
        )

        val result = models.asNotification()

        assertEquals(2, result.size)
        assertEquals(models[0].id, result[0].id)
        assertEquals(models[1].title, result[1].title)
    }
}
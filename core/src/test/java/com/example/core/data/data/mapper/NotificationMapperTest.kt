package com.example.core.data.data.mapper

import com.example.core.domain.model.NotificationModel
import com.example.core.data.local.room.entity.notification.NotificationEntity
import com.example.core.data.mapper.asNotificationEntity
import com.example.core.data.mapper.asNotificationModel

import kotlin.test.Test
import kotlin.test.assertEquals

class NotificationMapperTest {

    @Test
    fun `NotificationEntity should map correctly to NotificationModel`() {
        val entity = NotificationEntity(
            id = 1,
            title = "Promo Hari Ini!",
            body = "Diskon 50% untuk produk terpilih.",
            image = "https://example.com/image.jpg",
            type = "promo",
            date = "2025-06-16",
            time = "09:00",
            isRead = false
        )

        val model = entity.asNotificationModel()

        assertEquals(entity.id, model.id)
        assertEquals(entity.title, model.title)
        assertEquals(entity.body, model.body)
        assertEquals(entity.image, model.image)
        assertEquals(entity.type, model.type)
        assertEquals(entity.date, model.date)
        assertEquals(entity.time, model.time)
        assertEquals(entity.isRead, model.isRead)
    }

    @Test
    fun `NotificationModel should map correctly to NotificationEntity`() {
        val model = NotificationModel(
            id = 0,
            title = "Update Aplikasi",
            body = "Versi baru tersedia untuk diunduh.",
            image = "https://example.com/update.jpg",
            type = "update",
            date = "2025-06-15",
            time = "15:00",
            isRead = true
        )

        val entity = model.asNotificationEntity()

        assertEquals(model.id, entity.id)
        assertEquals(model.title, entity.title)
        assertEquals(model.body, entity.body)
        assertEquals(model.image, entity.image)
        assertEquals(model.type, entity.type)
        assertEquals(model.date, entity.date)
        assertEquals(model.time, entity.time)
        assertEquals(model.isRead, entity.isRead)
    }

    @Test
    fun `Entity to Model to Entity should preserve data`() {
        val originalEntity = NotificationEntity(
            id = 2,
            title = "Maintenance",
            body = "Akan ada pemeliharaan sistem malam ini.",
            image = "image",
            type = "maintenance",
            date = "2025-06-16",
            time = "22:00",
            isRead = false
        )

        val resultEntity = originalEntity.asNotificationModel().asNotificationEntity()

        assertEquals(originalEntity, resultEntity)
    }
}
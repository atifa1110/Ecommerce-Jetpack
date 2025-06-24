package com.example.core.data.data.local.datasource

import com.example.core.data.local.datasource.NotificationDatabaseSource
import com.example.core.domain.model.NotificationModel
import com.example.core.data.local.room.dao.notification.NotificationDao
import com.example.core.data.local.room.entity.notification.NotificationEntity
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class NotificationDatabaseSourceTest {

    private lateinit var notificationDao: NotificationDao
    private lateinit var dataSource: NotificationDatabaseSource

    @Before
    fun setup() {
        notificationDao = mockk(relaxed = true)
        dataSource = NotificationDatabaseSource(notificationDao)
    }

    private val model = NotificationModel(
        id = 1,
        title = "Test Title",
        body = "Test Message",
        image = "image",
        type = "promo",
        date = "", time = "",
        isRead = false,
    )

    @Test
    fun `addNotification should call dao with correct entity`() = runBlocking {
        dataSource.addNotification(model)

        coVerify {
            notificationDao.addNotification(
                match {
                    it.id == model.id &&
                            it.title == model.title &&
                            it.body == model.body &&
                            it.image == model.image &&
                            it.type == model.type &&
                            it.date == model.date &&
                            it.time == model.time &&
                            it.isRead == model.isRead
                }
            )
        }
    }

    @Test
    fun `getUnreadNotification returns correct unread count`() = runBlocking {
        val unreadList = listOf(
            NotificationEntity(id = 1, title = "Title", body = "Message", image = "image",
                type = "promo", date = "", time = "", isRead = false),
            NotificationEntity(id = 2, title = "Title1", body = "Message1", image = "image1",
                type = "promo", date = "", time = "", isRead = false)
        )
        coEvery { notificationDao.getNotificationRead(false) } returns flowOf(unreadList)

        val result = dataSource.getUnreadNotification()

        assertEquals(2, result)
        coVerify { notificationDao.getNotificationRead(false) }
    }

    @Test
    fun `getUnreadNotification returns 0 if no unread`() = runBlocking {
        coEvery { notificationDao.getNotificationRead(false) } returns flowOf(emptyList())
        val result = dataSource.getUnreadNotification()
        assertEquals(0, result)
    }

    @Test
    fun `updateReadNotification calls dao correctly`() = runBlocking {
        val id = 123
        val read = true

        dataSource.updateReadNotification(id, read)

        coVerify { notificationDao.updateReadNotification(id, read) }
    }
}

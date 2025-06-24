package com.example.core.data.data.local.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.core.data.local.room.dao.notification.NotificationDao
import com.example.core.data.local.room.database.EcommerceDatabase
import com.example.core.data.local.room.entity.notification.NotificationEntity
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NotificationDaoTest {

    private lateinit var database: EcommerceDatabase
    private lateinit var dao: NotificationDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database =
            Room.inMemoryDatabaseBuilder(context, EcommerceDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        dao = database.notificationDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `test add to notification room success`() {
        runBlocking {
            val entity = NotificationEntity(
                1,"Telkomsel Award 2023", "Nikmati kemeriahan ulang tahun Telkomsel",
                "image", "Promo", "21 Jul 2023", "12:34",false
            )

            dao.addNotification(entity)

            // Retrieve get all data
            val result = dao.getAllNotification().first()
            assertEquals(result.isNotEmpty(), true)
            assertEquals(1, result.size)
            assertEquals("Telkomsel Award 2023", result[0].title)
        }
    }

    @Test
    fun `test update read notification room success`() {
        runBlocking {
            val entity = NotificationEntity(
                1,"Telkomsel Award 2023", "Nikmati kemeriahan ulang tahun Telkomsel",
                "image", "Promo", "21 Jul 2023", "12:34",false
            )

            dao.addNotification(entity)
            dao.updateReadNotification(1,true)

            // Retrieve get all data
            val result = dao.getAllNotification().first()
            assertEquals(result.isNotEmpty(), true)
            assertEquals(1, result.size)
            assertEquals("Telkomsel Award 2023", result[0].title)
            assertEquals(true, result[0].isRead)
        }
    }

    @Test
    fun `test get notification read success`() {
        runBlocking {
            val entity = NotificationEntity(
                1,"Telkomsel Award 2023", "Nikmati kemeriahan ulang tahun Telkomsel",
                "image", "Promo", "21 Jul 2023", "12:34",false
            )

            val entity1 = NotificationEntity(
                2,"Telkomsel Award 2023", "Nikmati kemeriahan ulang tahun Telkomsel",
                "image", "Promo", "21 Jul 2023", "12:34",true
            )

            dao.addNotification(entity)
            dao.addNotification(entity1)

            // Retrieve get all data
            val result = dao.getNotificationRead(true).first()
            assertEquals(result.isNotEmpty(), true)
            assertEquals(1, result.size)
        }
    }

}
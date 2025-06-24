package com.example.core.data.data.repository.notification

import com.example.core.domain.model.NotificationModel
import com.example.core.data.local.datasource.NotificationDatabaseSource
import com.example.core.data.local.room.entity.notification.NotificationEntity
import com.example.core.data.repository.notification.NotificationRepositoryImpl
import com.example.core.data.network.response.EcommerceResponse
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class NotificationRepositoryImplTest {

    private val notificationDatabaseSource: NotificationDatabaseSource = mockk(relaxed = true)
    private lateinit var repository: NotificationRepositoryImpl

    @Before
    fun setup() {
        repository = NotificationRepositoryImpl(notificationDatabaseSource)
    }

    @Test
    fun `addNotification delegates to database source`() = runTest {
        val notification = NotificationModel(1, "title", "body", "image","promo","date","time",false)
        coEvery { notificationDatabaseSource.addNotification(notification) } just Runs

        repository.addNotification(notification)

        coVerify { notificationDatabaseSource.addNotification(notification) }
    }

    @Test
    fun `getNotification emits loading and success`() = runTest {
        val entities = listOf(
            NotificationEntity(1, "title", "body", "image","promo","date","time",false)
        )
        val flow = flowOf(entities)
        every { notificationDatabaseSource.getNotification() } returns flow

        val result = repository.getNotification().toList()

        assertTrue(result[0] is EcommerceResponse.Loading)
        assertTrue(result[1] is EcommerceResponse.Success)
        assertEquals(1, (result[1] as EcommerceResponse.Success).value.size)
    }

    @Test
    fun `getNotification emits failure when exception thrown`() = runTest {
        every { notificationDatabaseSource.getNotification() } throws RuntimeException("DB error")

        val result = repository.getNotification().toList()

        assertTrue(result[0] is EcommerceResponse.Loading)
        assertTrue(result[1] is EcommerceResponse.Failure)
        assertEquals("DB error", (result[1] as EcommerceResponse.Failure).error)
    }

    @Test
    fun `getUnreadNotification returns correct count`() = runTest {
        coEvery { notificationDatabaseSource.getUnreadNotification() } returns 5
        val result = repository.getUnreadNotification()

        assertEquals(5, result)
    }

    @Test
    fun `updateReadNotification delegates to database source`() = runTest {
        coEvery { notificationDatabaseSource.updateReadNotification(1, true) } just Runs
        repository.updateReadNotification(1, true)

        coVerify { notificationDatabaseSource.updateReadNotification(1, true) }
    }
}

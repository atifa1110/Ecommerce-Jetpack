package com.example.core.data.data.repository.messaging

import com.example.core.data.network.datasource.FirebaseNetworkDataSource
import com.example.core.data.repository.messaging.MessagingRepositoryImpl
import com.example.core.data.network.response.BaseResponse
import com.example.core.data.network.response.EcommerceResponse
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.messaging.FirebaseMessaging
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class MessagingRepositoryImplTest {

    private lateinit var firebaseNetworkDataSource: FirebaseNetworkDataSource
    private lateinit var messagingRepository: MessagingRepositoryImpl

    @Before
    fun setup() {
        mockkStatic(FirebaseMessaging::class)
        firebaseNetworkDataSource = mockk()
        messagingRepository = MessagingRepositoryImpl(firebaseNetworkDataSource)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `updateFcmToken emits success when network call succeeds`() = runTest {
        val response = BaseResponse(200,"Ok")
        coEvery { firebaseNetworkDataSource.updateFCMToken(any())
        } returns EcommerceResponse.Success(response)

        val result = messagingRepository.updateFcmToken().toList()

        assertTrue(result[0] is EcommerceResponse.Loading)
        assertEquals(EcommerceResponse.Success("Update is success"), result[1])
    }

    @Test
    fun `updateFcmToken emits failure when network call fails`() = runTest {
        coEvery { firebaseNetworkDataSource.updateFCMToken(any())
        } returns EcommerceResponse.Failure(401, "Unauthorized")

        val result = messagingRepository.updateFcmToken().toList()

        assertTrue(result[0] is EcommerceResponse.Loading)
        assertEquals(EcommerceResponse.Failure(401, "Unauthorized"), result[1])
    }

    @Test
    fun `getFirebasePhoneToken returns token`() = runTest {
        // Arrange
        val token = "fake_token"

        val firebaseMessagingMock = mockk<FirebaseMessaging>()
        val tokenTask = Tasks.forResult(token)

        every { FirebaseMessaging.getInstance() } returns firebaseMessagingMock
        every { firebaseMessagingMock.token } returns tokenTask

        // Act
        val result = messagingRepository.getFirebasePhoneToken()

        // Assert
        assertEquals(token, result)
    }

    @Test
    fun `subscribeFcmTopic returns true when subscription is successful`() {
        val firebaseMessagingMock = mockk<FirebaseMessaging>()
        every { FirebaseMessaging.getInstance() } returns firebaseMessagingMock
        val taskMock = mockk<Task<Void>>() {
            every { isSuccessful } returns true
        }
        every { firebaseMessagingMock.subscribeToTopic("promo") } returns taskMock

        val result = messagingRepository.subscribeFcmTopic()
        assertTrue(result)
    }

    @Test
    fun `subscribeFcmTopic returns false when subscription fails`() {
        val firebaseMessagingMock = mockk<FirebaseMessaging>()
        every { FirebaseMessaging.getInstance() } returns firebaseMessagingMock
        val taskMock = mockk<Task<Void>>() {
            every { isSuccessful } returns false
        }
        every { firebaseMessagingMock.subscribeToTopic("promo") } returns taskMock

        val result = messagingRepository.subscribeFcmTopic()
        assertFalse(result)
    }
}
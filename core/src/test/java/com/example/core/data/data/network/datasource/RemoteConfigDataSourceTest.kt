package com.example.core.data.data.network.datasource

import com.example.core.data.network.datasource.RemoteConfigDataSource
import com.example.core.data.network.model.PaymentNetwork
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.data.network.response.PaymentResponse
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class RemoteConfigDataSourceTest {

    private val remoteConfig: FirebaseRemoteConfig = mockk()
    private lateinit var dataSource: RemoteConfigDataSource

    @Before
    fun setUp() {
        dataSource = RemoteConfigDataSource(remoteConfig)
    }

    @Test
    fun `getPaymentConfig returns success when config is valid`() = runTest {
        // Arrange
        val responseObj = PaymentResponse(
            code = 200,
            message = "OK",
            data = listOf(
                PaymentNetwork(
                    title = "Bank Transfer",
                    item = listOf(PaymentNetwork.PaymentNetworkItem("BCA","image",true))
                )
            )
        )
        val json = Gson().toJson(responseObj)

        val task: Task<Boolean> = Tasks.forResult(true)

        coEvery { remoteConfig.fetchAndActivate() } returns task
        every { remoteConfig.getString("payment_config") } returns json

        // Act
        val result = dataSource.getPaymentConfig()

        // Assert
        assertTrue(result is EcommerceResponse.Success)
        assertEquals(200, result.value.code)
        assertEquals("OK", result.value.message)
        assertEquals(responseObj.data, result.value.data)
    }

    @Test
    fun `getPaymentConfig returns failure when code is not 200`() = runTest {
        val responseObj = PaymentResponse(
            code = 400,
            message = "Bad Request",
            data = emptyList()
        )

        val task: Task<Boolean> = Tasks.forResult(false)
        val json = Gson().toJson(responseObj)

        coEvery { remoteConfig.fetchAndActivate() } returns task
        every { remoteConfig.getString("payment_config") } returns json

        val result = dataSource.getPaymentConfig()

        assertTrue(result is EcommerceResponse.Failure)
        assertEquals(400, result.code)
        assertEquals("Bad Request", result.error)
    }

    @Test
    fun `getPaymentConfig returns failure on exception`() = runTest {
        coEvery { remoteConfig.fetchAndActivate() } throws RuntimeException("Firebase error")

        val result = dataSource.getPaymentConfig()

        assertTrue(result is EcommerceResponse.Failure)
        assertEquals(-1, result.code)
        assertEquals("Firebase error", result.error)
    }
}

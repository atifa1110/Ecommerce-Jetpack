package com.example.core.data.data.repository.payment

import com.example.core.domain.model.ItemTransactionModel
import com.example.core.data.network.datasource.PaymentNetworkDataSource
import com.example.core.data.network.datasource.RemoteConfigDataSource
import com.example.core.data.network.model.FulfillmentNetwork
import com.example.core.data.network.model.PaymentNetwork
import com.example.core.data.network.model.TransactionNetwork
import com.example.core.data.repository.payment.PaymentRepositoryImpl
import com.example.core.data.network.request.RatingRequest
import com.example.core.data.network.response.BaseResponse
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.data.network.response.FulfillmentResponse
import com.example.core.data.network.response.PaymentResponse
import com.example.core.data.network.response.TransactionResponse
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class PaymentRepositoryImplTest {

    var paymentNetworkDataSource: PaymentNetworkDataSource = mockk()
    var remoteConfigDataSource: RemoteConfigDataSource = mockk()

    private lateinit var repository: PaymentRepositoryImpl

    @Before
    fun setup() {
        repository = PaymentRepositoryImpl(paymentNetworkDataSource, remoteConfigDataSource)
    }

    @Test
    fun `getTransaction returns success`() = runTest {
        val transaction = TransactionResponse(200,"OK",
            listOf(TransactionNetwork("1",false,"date","time","BCA",13500000,listOf(),3,"","image","name")))

        coEvery { paymentNetworkDataSource.transaction() } returns EcommerceResponse.success(transaction)

        val result = repository.getTransaction().toList()

        assertTrue(result[0] is EcommerceResponse.Loading)
        assertTrue(result[1] is EcommerceResponse.Success)
        val success = (result[1] as EcommerceResponse.Success).value.size
        assertEquals(transaction.data.size,success)
    }

    @Test
    fun `getTransaction returns failure`() = runTest {
        coEvery { paymentNetworkDataSource.transaction() } returns
                EcommerceResponse.failure(404, "Not found")

        val result = repository.getTransaction().toList()
        assertTrue(result[0] is EcommerceResponse.Loading)
        assertTrue(result[1] is EcommerceResponse.Failure)
        val failure = (result[1] as EcommerceResponse.Failure)
        assertEquals(404,failure.code)
    }

    @Test
    fun `fulfillmentTransaction returns success`() = runTest {
        val expectedData = FulfillmentResponse(200,"Ok", FulfillmentNetwork("1",true,"date","time","BCA",15000000))
        coEvery { paymentNetworkDataSource.fulfillment(any()) } returns EcommerceResponse.success(expectedData)

        val result = repository.fulfillmentTransaction("payment", listOf(ItemTransactionModel("1","RAM 16GB",1))).toList()

        assertTrue(result[0] is EcommerceResponse.Loading)
        assertTrue(result[1] is EcommerceResponse.Success)
        val success = (result[1] as EcommerceResponse.Success).value
        assertEquals(expectedData.data.invoiceId,success.invoiceId)
        assertEquals(expectedData.data.date,success.date)
    }

    @Test
    fun `fulfillmentTransaction returns failure`() = runTest {
        coEvery { paymentNetworkDataSource.fulfillment(any()) } returns EcommerceResponse.failure(400, "Bad request")

        val result = repository.fulfillmentTransaction("payment", listOf(ItemTransactionModel("1","RAM 16GB",1))).toList()
        assertTrue(result[0] is EcommerceResponse.Loading)
        assertTrue(result[1] is EcommerceResponse.Failure)
        val failure = (result[1] as EcommerceResponse.Failure)
        assertEquals(400,failure.code)
    }

    @Test
    fun `setRatingTransaction returns success`() = runTest {
        val request = RatingRequest("INV-001", 5, "Excellent")
        coEvery { paymentNetworkDataSource.rating(request) } returns EcommerceResponse.success(BaseResponse(200,"OK"))

        val result = repository.setRatingTransaction("INV-001", 5, "Excellent").toList()
        assertTrue(result[0] is EcommerceResponse.Loading)
        assertTrue(result[1] is EcommerceResponse.Success)
        val success = (result[1] as EcommerceResponse.Success).value
        assertEquals("OK",success)
    }

    @Test
    fun `setRatingTransaction returns failure`() = runTest {
        val request = RatingRequest("INV-001", 5, "Excellent")
        coEvery { paymentNetworkDataSource.rating(request) } returns EcommerceResponse.failure(500, "Server error")

        val result = repository.setRatingTransaction("INV-001", 5, "Excellent").toList()
        assertTrue(result[0] is EcommerceResponse.Loading)
        assertTrue(result[1] is EcommerceResponse.Failure)
        val failure = (result[1] as EcommerceResponse.Failure)
        assertEquals(500,failure.code)
    }


    @Test
    fun `getPaymentConfig returns success`() = runTest {
        val expectedData = PaymentResponse(200,"OK",listOf(PaymentNetwork("Bank Transfer",
            listOf(PaymentNetwork.PaymentNetworkItem("BCA","image",true)))))
        coEvery { remoteConfigDataSource.getPaymentConfig()
        } returns EcommerceResponse.success(expectedData)

        val result = repository.getPaymentConfig().toList()
        assertTrue(result[0] is EcommerceResponse.Loading)
        assertTrue(result[1] is EcommerceResponse.Success)
        val success = (result[1] as EcommerceResponse.Success)
        assertEquals(expectedData.data.size,success.value.size)
    }

    @Test
    fun `getPaymentConfig returns failure`() = runTest {
        coEvery { remoteConfigDataSource.getPaymentConfig()
        } returns EcommerceResponse.failure(403, "Forbidden")

        val result = repository.getPaymentConfig().toList()
        assertTrue(result[0] is EcommerceResponse.Loading)
        assertTrue(result[1] is EcommerceResponse.Failure)
        val failure = (result[1] as EcommerceResponse.Failure)
        assertEquals(403,failure.code)
    }


}
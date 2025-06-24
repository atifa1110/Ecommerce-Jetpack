package com.example.core.data.data.network.datasource

import com.example.core.data.network.api.PaymentApiService
import com.example.core.data.network.datasource.PaymentNetworkDataSource
import com.example.core.data.network.model.FulfillmentNetwork
import com.example.core.data.network.model.ItemTransactionNetwork
import com.example.core.data.network.model.TransactionNetwork
import com.example.core.data.network.request.FulfillmentRequest
import com.example.core.data.network.request.RatingRequest
import com.example.core.data.network.response.BaseResponse
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.data.network.response.FulfillmentResponse
import com.example.core.data.network.response.TransactionResponse
import com.google.gson.Gson
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import java.io.IOException
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class PaymentNetworkDataSourceTest {

    private lateinit var paymentApiService: PaymentApiService
    private lateinit var source: PaymentNetworkDataSource
    private val gson = Gson()

    @BeforeTest
    fun setup() {
        paymentApiService = mockk()
        source = PaymentNetworkDataSource(paymentApiService, gson)
    }

    @Test
    fun `fulfillment returns success on 200 OK`() = runTest {
        val request = FulfillmentRequest("BCA",listOf(ItemTransactionNetwork("1","RAM 16GB",1)))
        val expectedData = FulfillmentResponse(200,"Ok", FulfillmentNetwork("1",true,"date","time","BCA",15000000))

        coEvery { paymentApiService.fulfillment(request) }returns Response.success(expectedData)

        val result = source.fulfillment(request)

        assertTrue(result is EcommerceResponse.Success)
        assertEquals(expectedData.code,result.value.code)
        assertEquals(expectedData.message,result.value.message)
        assertEquals(expectedData.data,result.value.data)
    }

    @Test
    fun `fulfillment returns error on 400`() = runTest {
        val request = FulfillmentRequest("BCA",listOf())
        val errorJson = """{"code":400,"message":"Bad Request"}"""
        val errorBody = errorJson.toResponseBody("application/json".toMediaTypeOrNull())
        coEvery { paymentApiService.fulfillment(request) } returns Response.error(400, errorBody)

        val result = source.fulfillment(request)

        assertTrue(result is EcommerceResponse.Failure)
        assertEquals(400, result.code)
        assertEquals("Bad Request", result.error)
    }

    @Test
    fun `transaction returns success on 200 OK`() = runTest {
        val expectedData = TransactionResponse(200,"Ok",
            listOf(TransactionNetwork("1",false,"date","time","BCA",13500000,listOf(),3,"","image","name")))
        coEvery { paymentApiService.transaction() } returns Response.success(expectedData)

        val result = source.transaction()

        assertTrue(result is EcommerceResponse.Success)
        assertEquals(expectedData.code,result.value.code)
        assertEquals(expectedData.message,result.value.message)
        assertEquals(expectedData.data,result.value.data)
    }

    @Test
    fun `transaction returns server error on 500`() = runTest {
        val errorJson = """{"code":500,"message":"Internal server error"}"""
        val errorBody = errorJson.toResponseBody("application/json".toMediaTypeOrNull())
        coEvery { paymentApiService.transaction() } returns Response.error(500, errorBody)

        val result = source.transaction()

        assertTrue(result is EcommerceResponse.Failure)
        assertEquals(500, result.code)
        assertEquals("Internal server error", result.error)
    }

    @Test
    fun `rating returns success on 200 OK`() = runTest {
        val ratingRequest = RatingRequest("1",4,"")
        val baseResponse = BaseResponse(200, "Success")

        coEvery { paymentApiService.rating(ratingRequest) } returns Response.success(baseResponse)

        val result = source.rating(ratingRequest)

        assertTrue(result is EcommerceResponse.Success)
        assertEquals(baseResponse.message, result.value.message)
    }


    @Test
    fun `rating throws exception returns error`() = runTest {
        val ratingRequest = RatingRequest("1",4,"")
        coEvery { paymentApiService.rating(ratingRequest) } throws IOException("Timeout")

        val result = source.rating(ratingRequest)

        assertTrue(result is EcommerceResponse.Failure)
        assertEquals("Timeout", result.error)
    }

}

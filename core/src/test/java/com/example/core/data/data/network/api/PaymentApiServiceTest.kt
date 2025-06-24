package com.example.core.data.data.network.api

import com.example.core.data.network.api.PaymentApiService
import com.example.core.data.network.model.FulfillmentNetwork
import com.example.core.data.network.model.ItemTransactionNetwork
import com.example.core.data.network.model.TransactionNetwork
import com.example.core.data.network.request.FulfillmentRequest
import com.example.core.data.network.request.RatingRequest
import com.example.core.data.network.response.BaseResponse
import com.example.core.data.network.response.ErrorResponse
import com.example.core.data.network.response.FulfillmentResponse
import com.example.core.data.network.response.TransactionResponse
import com.google.gson.Gson
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(JUnit4::class)
class PaymentApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: PaymentApiService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        val client = OkHttpClient.Builder().build()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(PaymentApiService::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `test fulfillment success`() {
        mockWebServer.enqueueResponse("Fulfillment.json", 200)
        runBlocking {
            val actual = apiService.fulfillment(
                FulfillmentRequest(
                    "", listOf(
                        ItemTransactionNetwork("b774d021-250a-4c3a-9c58-ab39edb36de5", "RAM 16GB", 1)
                    )
                )
            ).body()
            val expected = FulfillmentResponse(
                200,
                "OK",
                FulfillmentNetwork(
                    "c0aecf56-911c-4d4e-a5a2-f2ecd20f86be",
                    true,
                    "26 Sep 2023",
                    "15:03",
                    "Bank BCA",
                    13849000
                )
            )

            assertEquals(expected, actual)
        }
    }

    @Test
    fun `test fulfillment fails user invalid`() {
        mockWebServer.enqueueResponse("ProfileErrorUser.json", 403)
        runBlocking {
            val actual = apiService.fulfillment(
                FulfillmentRequest(
                    "BCA",
                    listOf(
                        ItemTransactionNetwork(
                            "b774d021-250a-4c3a-9c58-ab39edb36de5",
                            "RAM 16GB", 1
                        )
                    )
                )
            )

            // ✅ Check status code and error state
            assertEquals(false, actual.isSuccessful)
            assertEquals(403, actual.code())

            // ✅ Optional: parse error body to check message
            val errorJson = actual.errorBody()?.string()
            println("Error Body: $errorJson") // Debugging

            // You can use a JSON parser to map this to ErrorResponse if needed
            // e.g., using Gson:
            val errorResponse = Gson().fromJson(errorJson, ErrorResponse::class.java)
            assertEquals("User invalid", errorResponse.message)
        }
    }

    @Test
    fun `test fulfillment fails payment item invalid`() {
        mockWebServer.enqueueResponse("FulfillmentErrorPaymentItem.json", 400)
        runBlocking {
            val actual = apiService.fulfillment(
                FulfillmentRequest(
                    "",
                    listOf(
                        ItemTransactionNetwork(
                            "b774d021-250a-4c3a-9c58-ab39edb36de5",
                            "RAM 16GB", 1
                        )
                    )
                )
            )

            // ✅ Check status code and error state
            assertEquals(false, actual.isSuccessful)
            assertEquals(400, actual.code())

            // ✅ Optional: parse error body to check message
            val errorJson = actual.errorBody()?.string()
            println("Error Body: $errorJson") // Debugging

            // You can use a JSON parser to map this to ErrorResponse if needed
            // e.g., using Gson:
            val errorResponse = Gson().fromJson(errorJson, ErrorResponse::class.java)
            assertEquals("Payment or Items must be filled correctly", errorResponse.message)
        }
    }

    @Test
    fun `test transaction success`() {
        mockWebServer.enqueueResponse("Transaction.json", 200)
        runBlocking {
            val actual = apiService.transaction().body()
            val expected = TransactionResponse(
                200,
                "OK",
                listOf(
                    TransactionNetwork(
                        "c0aecf56-911c-4d4e-a5a2-f2ecd20f86be",
                        true, "26 Sep 2023", "15:03",
                        "Bank BCA", 13849000,
                        listOf(
                            ItemTransactionNetwork(
                                "b774d021-250a-4c3a-9c58-ab39edb36de5",
                                "RAM 16GB",
                                1
                            )
                        ), 0, "", "image", "name"
                    )
                )
            )

            assertEquals(expected, actual)
            assertEquals(1, actual?.data?.size)
        }
    }

    @Test
    fun `test transaction fails user invalid`() {
        mockWebServer.enqueueResponse("ProfileErrorUser.json", 403)
        runBlocking {
            val actual = apiService.transaction()
            // ✅ Check status code and error state
            assertEquals(false, actual.isSuccessful)
            assertEquals(403, actual.code())

            // ✅ Optional: parse error body to check message
            val errorJson = actual.errorBody()?.string()
            println("Error Body: $errorJson") // Debugging

            // You can use a JSON parser to map this to ErrorResponse if needed
            // e.g., using Gson:
            val errorResponse = Gson().fromJson(errorJson, ErrorResponse::class.java)
            assertEquals("User invalid", errorResponse.message)
        }
    }

    @Test
    fun `test transaction fails not found`() {
        mockWebServer.enqueueResponse("FulfillmentErrorPaymentItem.json", 400)
        runBlocking {
            val actual = apiService.transaction()

            // ✅ Check status code and error state
            assertEquals(false, actual.isSuccessful)
            assertEquals(400, actual.code())

            // ✅ Optional: parse error body to check message
            val errorJson = actual.errorBody()?.string()
            println("Error Body: $errorJson") // Debugging

            // You can use a JSON parser to map this to ErrorResponse if needed
            // e.g., using Gson:
            val errorResponse = Gson().fromJson(errorJson, ErrorResponse::class.java)
            assertEquals("Payment or Items must be filled correctly", errorResponse.message)
        }
    }

    @Test
    fun `test rating success`() {
        mockWebServer.enqueueResponse("Rating.json", 200)
        runBlocking {
            val actual = apiService.rating(
                RatingRequest(
                    "c0aecf56-911c-4d4e-a5a2-f2ecd20f86be",
                    5,
                    "This laptop is good ok"
                )
            ).body()
            val expected = BaseResponse(
                200, "Fulfillment rating and review success"
            )

            assertEquals(expected, actual)
            assertEquals("Fulfillment rating and review success", actual?.message)

        }
    }

    @Test
    fun `test rating fails user or product invalid`() {
        mockWebServer.enqueueResponse("RatingErrorUserInvalid.json", 403)
        runBlocking {
            val actual = apiService.transaction()
            // ✅ Check status code and error state
            assertEquals(false, actual.isSuccessful)
            assertEquals(403, actual.code())

            // ✅ Optional: parse error body to check message
            val errorJson = actual.errorBody()?.string()
            println("Error Body: $errorJson") // Debugging

            // You can use a JSON parser to map this to ErrorResponse if needed
            // e.g., using Gson:
            val errorResponse = Gson().fromJson(errorJson, ErrorResponse::class.java)
            assertEquals("User or Product invalid", errorResponse.message)
        }
    }

    @Test
    fun `test rating fails not found`() {
        mockWebServer.enqueueResponse("FulfillmentErrorPaymentItem.json", 400)
        runBlocking {
            val actual = apiService.transaction()

            // ✅ Check status code and error state
            assertEquals(false, actual.isSuccessful)
            assertEquals(400, actual.code())

            // ✅ Optional: parse error body to check message
            val errorJson = actual.errorBody()?.string()
            println("Error Body: $errorJson") // Debugging

            // You can use a JSON parser to map this to ErrorResponse if needed
            // e.g., using Gson:
            val errorResponse = Gson().fromJson(errorJson, ErrorResponse::class.java)
            assertEquals("Payment or Items must be filled correctly", errorResponse.message)
        }
    }
}
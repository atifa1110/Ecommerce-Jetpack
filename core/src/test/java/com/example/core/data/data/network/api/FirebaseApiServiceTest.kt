package com.example.core.data.data.network.api

import com.example.core.data.network.api.FirebaseApiService
import com.example.core.data.network.request.AuthRequest
import com.example.core.data.network.request.TokenRequest
import com.example.core.data.network.response.BaseResponse
import com.example.core.data.network.response.ErrorResponse
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
class FirebaseApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: FirebaseApiService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        val client = OkHttpClient.Builder().build()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(FirebaseApiService::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `test firebase token success`() {
        mockWebServer.enqueueResponse("FirebaseToken.json", 200)
        runBlocking {
            val request = TokenRequest("token")
            val actual = apiService.firebaseToken(request).body()
            val expected = BaseResponse(
                200,"Firebase token updated"
            )

            assertEquals(expected, actual)
        }
    }

    @Test
    fun `test firebase token fail`() {
        mockWebServer.enqueueResponse("ApiKeyError403.json", 403)
        runBlocking {
            val request = TokenRequest(" ")
            val actual = apiService.firebaseToken(request)
            // ✅ Check status code and error state
            assertEquals(false, actual.isSuccessful)
            assertEquals(403, actual.code())

            // ✅ Optional: parse error body to check message
            val errorJson = actual.errorBody()?.string()
            println("Error Body: $errorJson") // Debugging

            // You can use a JSON parser to map this to ErrorResponse if needed
            // e.g., using Gson:
            val errorResponse = Gson().fromJson(errorJson, ErrorResponse::class.java)
            assertEquals("Api key cannot be null", errorResponse.message)
        }
    }
}
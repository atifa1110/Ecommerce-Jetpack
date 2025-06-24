package com.example.core.data.data.network.api

import com.example.core.data.network.api.AuthApiService
import com.example.core.data.network.model.LoginNetwork
import com.example.core.data.network.model.TokenNetwork
import com.example.core.data.network.model.UserNetwork
import com.example.core.data.network.request.AuthRequest
import com.example.core.data.network.request.TokenRequest
import com.example.core.data.network.response.ErrorResponse
import com.example.core.data.network.response.LoginResponse
import com.example.core.data.network.response.ProfileResponse
import com.example.core.data.network.response.RefreshResponse
import com.example.core.data.network.response.RegisterResponse
import com.google.gson.Gson
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.nio.charset.StandardCharsets

@RunWith(JUnit4::class)
class AuthApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: AuthApiService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        val client = OkHttpClient.Builder().build()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(AuthApiService::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `test login success`() {
        mockWebServer.enqueueResponse("Login.json", 200)
        runBlocking {
            val actual = apiService.loginUser(
                AuthRequest("test@gmail.com", "12345678", "")
            ).body()
            val expected = LoginResponse(200, "OK",
                LoginNetwork("Test", "Image", "123", "123", 3600)
            )
            assertEquals(expected, actual)
            assertEquals("Test",actual?.data?.userName)
        }
    }

    @Test
    fun `test login fails with invalid email password`() {
        mockWebServer.enqueueResponse("LoginError400.json", 400)
        runBlocking {
            val actual = apiService.loginUser(
                AuthRequest("test@gmail.com", "12345678", "")
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
            assertEquals("Email or password is not valid", errorResponse.message)
        }
    }

    @Test
    fun `test login fails with null api key`() {
        mockWebServer.enqueueResponse("ApiKeyError403.json", 403)
        runBlocking {
            val actual = apiService.loginUser(
                AuthRequest("test@gmail.com", "12345678", "")
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
            assertEquals("Api key cannot be null", errorResponse.message)
        }
    }

    @Test
    fun `test login fails with invalid api key`() {
        mockWebServer.enqueueResponse("InvalidKeyError403.json", 403)
        runBlocking {
            val actual = apiService.loginUser(
                AuthRequest("test@gmail.com", "12345678", "")
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
            assertEquals("Api key is not valid", errorResponse.message)
        }
    }

    @Test
    fun `test register success`() {
        mockWebServer.enqueueResponse("Register.json", 200)
        runBlocking {
            val actual = apiService.registerUser(
                AuthRequest("test@gmail.com", "12345678", "")
            ).body()
            val expected = RegisterResponse(200, "OK", TokenNetwork("123", "123", 3600))

            assertEquals(expected, actual)
            assertEquals("123",actual?.data?.accessToken)
        }
    }

    @Test
    fun `test register error email is taken`() {
        mockWebServer.enqueueResponse("RegisterError400.json", 400)
        runBlocking {
            val actual = apiService.loginUser(
                AuthRequest("test@gmail.com", "12345678", "")
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
            assertEquals("Email is already taken", errorResponse.message)
        }
    }

    @Test
    fun `test register fails with null api key`() {
        mockWebServer.enqueueResponse("ApiKeyError403.json", 403)
        runBlocking {
            val actual = apiService.loginUser(
                AuthRequest("test@gmail.com", "12345678", "")
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
            assertEquals("Api key cannot be null", errorResponse.message)
        }
    }

    @Test
    fun `test register fails with invalid api key`() {
        mockWebServer.enqueueResponse("InvalidKeyError403.json", 403)
        runBlocking {
            val actual = apiService.registerUser(
                AuthRequest("test@gmail.com", "12345678", "")
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
            assertEquals("Api key is not valid", errorResponse.message)
        }
    }

    @Test
    fun `test refresh token success service`() {
        mockWebServer.enqueueResponse("Refresh.json", 200)
        runBlocking {
            val actual = apiService.refreshToken(TokenRequest("token")).body()
            val expected = RefreshResponse(200, "OK", TokenNetwork("123", "123", 3600))
            assertEquals(expected, actual)
            assertEquals("123",actual?.data?.accessToken)
        }
    }

    @Test
    fun `test refresh fails with token invalid`() {
        mockWebServer.enqueueResponse("TokenError401.json", 401)
        runBlocking {
            val actual = apiService.refreshToken(TokenRequest("token"))
            // ✅ Check status code and error state
            assertEquals(false, actual.isSuccessful)
            assertEquals(401, actual.code())

            // ✅ Optional: parse error body to check message
            val errorJson = actual.errorBody()?.string()
            println("Error Body: $errorJson") // Debugging

            // You can use a JSON parser to map this to ErrorResponse if needed
            // e.g., using Gson:
            val errorResponse = Gson().fromJson(errorJson, ErrorResponse::class.java)
            assertEquals("Token is not valid or has expired", errorResponse.message)
        }
    }

    @Test
    fun `test refresh fails with null api key`() {
        mockWebServer.enqueueResponse("ApiKeyError403.json", 403)
        runBlocking {
            val actual = apiService.refreshToken(TokenRequest("token"))
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

    @Test
    fun `test refresh fails with invalid api key`() {
        mockWebServer.enqueueResponse("InvalidKeyError403.json", 403)
        runBlocking {
            val actual = apiService.refreshToken(TokenRequest("token"))
            // ✅ Check status code and error state
            assertEquals(false, actual.isSuccessful)
            assertEquals(403, actual.code())

            // ✅ Optional: parse error body to check message
            val errorJson = actual.errorBody()?.string()
            println("Error Body: $errorJson") // Debugging

            // You can use a JSON parser to map this to ErrorResponse if needed
            // e.g., using Gson:
            val errorResponse = Gson().fromJson(errorJson, ErrorResponse::class.java)
            assertEquals("Api key is not valid", errorResponse.message)
        }
    }

    @Test
    fun `test profile success`() {
        mockWebServer.enqueueResponse("Profile.json", 200)
        runBlocking {
            val dummyBody = "Test".toRequestBody("text/plain".toMediaTypeOrNull())
            val image = MultipartBody.Part.createFormData("userImage", "test.jpg", dummyBody)
            val name = MultipartBody.Part.createFormData("userName", "Test")
            val actual = apiService.profileUser(image, name).body()
            val expected = ProfileResponse(
                200, "OK", UserNetwork("Test", "http://192.168.190.125:8080/static/images/")
            )

            assertEquals(expected, actual)
            assertEquals("Test", actual?.data?.userName)
        }
    }

    @Test
    fun `test profile fails request invalid`() {
        mockWebServer.enqueueResponse("ProfileErrorRequest.json", 400)
        runBlocking {
            val dummyBody = "Test".toRequestBody("text/plain".toMediaTypeOrNull())
            val image = MultipartBody.Part.createFormData("userImage", "test.jpg", dummyBody)
            val name = MultipartBody.Part.createFormData("userName", "Test")
            val actual = apiService.profileUser(image, name)
            // ✅ Check status code and error state
            assertEquals(false, actual.isSuccessful)
            assertEquals(400, actual.code())

            // ✅ Optional: parse error body to check message
            val errorJson = actual.errorBody()?.string()
            println("Error Body: $errorJson") // Debugging

            // You can use a JSON parser to map this to ErrorResponse if needed
            // e.g., using Gson:
            val errorResponse = Gson().fromJson(errorJson, ErrorResponse::class.java)
            assertEquals("Request must be userName or userImage", errorResponse.message)
        }
    }

    @Test
    fun `test profile fails username invalid`() {
        mockWebServer.enqueueResponse("ProfileErrorUsername.json", 400)
        runBlocking {
            val dummyBody = "Test".toRequestBody("text/plain".toMediaTypeOrNull())
            val image = MultipartBody.Part.createFormData("userImage", "test.jpg", dummyBody)
            val name = MultipartBody.Part.createFormData("userName", "Test")
            val actual = apiService.profileUser(image, name)
            // ✅ Check status code and error state
            assertEquals(false, actual.isSuccessful)
            assertEquals(400, actual.code())

            // ✅ Optional: parse error body to check message
            val errorJson = actual.errorBody()?.string()
            println("Error Body: $errorJson") // Debugging

            // You can use a JSON parser to map this to ErrorResponse if needed
            // e.g., using Gson:
            val errorResponse = Gson().fromJson(errorJson, ErrorResponse::class.java)
            assertEquals("Username cannot be null", errorResponse.message)
        }
    }

    @Test
    fun `test profile fails user invalid`() {
        mockWebServer.enqueueResponse("ProfileErrorUser.json", 403)
        runBlocking {
            val dummyBody = "Test".toRequestBody("text/plain".toMediaTypeOrNull())
            val image = MultipartBody.Part.createFormData("userImage", "test.jpg", dummyBody)
            val name = MultipartBody.Part.createFormData("userName", "Test")
            val actual = apiService.profileUser(image, name)
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
}

fun MockWebServer.enqueueResponse(fileName: String, code: Int) {
    val inputStream = javaClass.classLoader?.getResourceAsStream(fileName)
    val source = inputStream?.let { inputStream.source().buffer() }
    source?.let {
        enqueue(
            MockResponse()
                .setResponseCode(code)
                .setBody(source.readString(StandardCharsets.UTF_8))
        )
    }
}

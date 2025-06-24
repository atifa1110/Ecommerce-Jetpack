package com.example.core.data.data.network.datasource

import com.example.core.data.network.api.AuthApiService
import com.example.core.data.network.api.FirebaseApiService
import com.example.core.data.network.datasource.AuthNetworkDataSource
import com.example.core.data.network.model.LoginNetwork
import com.example.core.data.network.model.TokenNetwork
import com.example.core.data.network.model.UserNetwork
import com.example.core.data.network.request.AuthRequest
import com.example.core.data.network.request.TokenRequest
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.data.network.response.LoginResponse
import com.example.core.data.network.response.ProfileResponse
import com.example.core.data.network.response.RefreshResponse
import com.example.core.data.network.response.RegisterResponse
import com.google.gson.Gson
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Response
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class AuthNetworkDataSourceTest {

    private val authApiService: AuthApiService = mockk()
    private val firebaseApiService: FirebaseApiService = mockk()
    private val gson = Gson()

    private lateinit var source: AuthNetworkDataSource

    @Before
    fun setUp() {
        source = AuthNetworkDataSource(authApiService, firebaseApiService, gson)
    }

    @Test
    fun `loginUser returns success when API responds successfully`() = runTest {
        // Given
        val request = AuthRequest("email@example.com","password","token")
        val expectedData = LoginResponse(
            200,"Ok", LoginNetwork("Test","image","access-token", "refresh-token",3600)
        )
        val response: Response<LoginResponse> = Response.success(expectedData)
        coEvery { authApiService.loginUser(request) } returns response

        // When
        val result = source.loginUser(request)

        // Then
        assertTrue(result is EcommerceResponse.Success)
        assertEquals(expectedData.code,result.value.code)
        assertEquals(expectedData.data, result.value.data)
    }

    @Test
    fun `loginUser returns error on 400 Bad Request`() = runTest {
        val request = AuthRequest("email@example.com", "wrong-password", "token")
        val errorJson = """{ "code": 401,"message": "Invalid credentials"}""".trimIndent()
        val errorBody = errorJson.toResponseBody("application/json".toMediaTypeOrNull())

        coEvery { authApiService.loginUser(request) } returns Response.error(401, errorBody)

        val result = source.loginUser(request)

        assertTrue(result is EcommerceResponse.Failure)
        assertEquals(401, result.code)
        assertEquals("Invalid credentials", result.error)
    }

    @Test
    fun `loginUser returns error on 500 Internal Server Error`() = runTest {
        val request = AuthRequest("email@example.com", "wrong-password", "token")
        val errorJson = """{"code":500,"message":"Server failure"}""".trimIndent()
        val errorBody = errorJson.toResponseBody("application/json".toMediaTypeOrNull())

        coEvery { authApiService.loginUser(any()) } returns Response.error(500, errorBody)
        val result = source.loginUser(request)

        assertTrue(result is EcommerceResponse.Failure)
        assertEquals(500, result.code)
        assertEquals("Server failure", result.error)
    }

    @Test
    fun `loginUser returns error when exception is thrown`() = runTest {
        val request = AuthRequest("email@example.com", "password","token")

        coEvery { authApiService.loginUser(request) } throws RuntimeException("Timeout")

        val result = source.loginUser(request)

        assertTrue(result is EcommerceResponse.Failure)
        assertEquals("Timeout", result.error)
    }

    //register
    @Test
    fun `registerUser returns success when API responds successfully`() = runTest {
        // Given
        val request = AuthRequest("email@example.com","password","token")
        val expectedData = RegisterResponse(
            200,"Ok", TokenNetwork("access-token", "refresh-token",3600)
        )
        val response: Response<RegisterResponse> = Response.success(expectedData)
        coEvery { authApiService.registerUser(request) } returns response

        // When
        val result = source.registerUser(request)

        // Then
        assertTrue(result is EcommerceResponse.Success)
        assertEquals(expectedData.code,result.value.code)
        assertEquals(expectedData.data, result.value.data)
    }

    @Test
    fun `registerUser returns error on 400 Bad Request`() = runTest {
        val request = AuthRequest("email@example.com", "wrong-password", "token")
        val errorJson = """{ "code": 401,"message": "Invalid credentials"}""".trimIndent()
        val errorBody = errorJson.toResponseBody("application/json".toMediaTypeOrNull())

        coEvery { authApiService.registerUser(request) } returns Response.error(401, errorBody)

        val result = source.registerUser(request)

        assertTrue(result is EcommerceResponse.Failure)
        assertEquals(401, result.code)
        assertEquals("Invalid credentials", result.error)
    }

    @Test
    fun `registerUser returns error on 500 Internal Server Error`() = runTest {
        val request = AuthRequest("email@example.com", "wrong-password", "token")
        val errorJson = """{"code":500,"message":"Server failure"}""".trimIndent()
        val errorBody = errorJson.toResponseBody("application/json".toMediaTypeOrNull())

        coEvery { authApiService.registerUser(any()) } returns Response.error(500, errorBody)
        val result = source.registerUser(request)

        assertTrue(result is EcommerceResponse.Failure)
        assertEquals(500, result.code)
        assertEquals("Server failure", result.error)
    }

    @Test
    fun `registerUser returns error when exception is thrown`() = runTest {
        val request = AuthRequest("email@example.com", "password","token")

        coEvery { authApiService.registerUser(request) } throws RuntimeException("Timeout")

        val result = source.registerUser(request)

        assertTrue(result is EcommerceResponse.Failure)
        assertEquals("Timeout", result.error)
    }

    //refresh
    @Test
    fun `refreshToken returns success when API responds successfully`() = runTest {
        // Given
        val request = TokenRequest("token")
        val expectedData = RefreshResponse(
            200,"Ok", TokenNetwork("access-token", "refresh-token",3600)
        )
        val response: Response<RefreshResponse> = Response.success(expectedData)
        coEvery { authApiService.refreshToken(request) } returns response

        // When
        val result = source.refreshToken(request)

        // Then
        assertTrue(result is EcommerceResponse.Success)
        assertEquals(expectedData.code,result.value.code)
        assertEquals(expectedData.data, result.value.data)
    }

    @Test
    fun `refreshToken returns error on 400 Bad Request`() = runTest {
        val request = TokenRequest("token")
        val errorJson = """{ "code": 401,"message": "Invalid credentials"}""".trimIndent()
        val errorBody = errorJson.toResponseBody("application/json".toMediaTypeOrNull())

        coEvery { authApiService.refreshToken(request) } returns Response.error(401, errorBody)

        val result = source.refreshToken(request)

        assertTrue(result is EcommerceResponse.Failure)
        assertEquals(401, result.code)
        assertEquals("Invalid credentials", result.error)
    }

    @Test
    fun `refreshToken returns error on 500 Internal Server Error`() = runTest {
        val request = TokenRequest("token")
        val errorJson = """{"code":500,"message":"Server failure"}""".trimIndent()
        val errorBody = errorJson.toResponseBody("application/json".toMediaTypeOrNull())

        coEvery { authApiService.refreshToken(request) } returns Response.error(500, errorBody)
        val result = source.refreshToken(request)

        assertTrue(result is EcommerceResponse.Failure)
        assertEquals(500, result.code)
        assertEquals("Server failure", result.error)
    }

    @Test
    fun `refreshToken returns error when exception is thrown`() = runTest {
        val request = TokenRequest("token")

        coEvery { authApiService.refreshToken(request) } throws RuntimeException("Timeout")

        val result = source.refreshToken(request)

        assertTrue(result is EcommerceResponse.Failure)
        assertEquals("Timeout", result.error)
    }

    //profile
    @Test
    fun `profileUser returns success when API responds successfully`() = runTest {
        // Given
        val dummyRequestBody = "dummy".toRequestBody("text/plain".toMediaTypeOrNull())
        val userNamePart = MultipartBody.Part.createFormData("userName", "John Doe", dummyRequestBody)
        val imagePart = MultipartBody.Part.createFormData("userImage", "img.jpg", dummyRequestBody)

        val expectedData = ProfileResponse(
            200,"Ok", UserNetwork("John Doe", "img.jpg")
        )
        val response: Response<ProfileResponse> = Response.success(expectedData)
        coEvery { authApiService.profileUser(imagePart,userNamePart) } returns response

        // When
        val result = source.profileUser(imagePart,userNamePart)

        // Then
        assertTrue(result is EcommerceResponse.Success)
        assertEquals(expectedData.code,result.value.code)
        assertEquals(expectedData.data.userName, result.value.data.userName)
        assertEquals(expectedData.data.userImage, result.value.data.userImage)
    }

    @Test
    fun `profileUser returns error on 400 Bad Request`() = runTest {
        val dummyRequestBody = "dummy".toRequestBody("text/plain".toMediaTypeOrNull())
        val userNamePart = MultipartBody.Part.createFormData("userName", "John Doe", dummyRequestBody)
        val imagePart = MultipartBody.Part.createFormData("userImage", "img.jpg", dummyRequestBody)

        val errorJson = """{ "code": 401,"message": "Invalid credentials"}""".trimIndent()
        val errorBody = errorJson.toResponseBody("application/json".toMediaTypeOrNull())

        coEvery { authApiService.profileUser(imagePart,userNamePart) } returns Response.error(401, errorBody)

        val result = source.profileUser(imagePart,userNamePart)

        assertTrue(result is EcommerceResponse.Failure)
        assertEquals(401, result.code)
        assertEquals("Invalid credentials", result.error)
    }

    @Test
    fun `profileUser returns error on 500 Internal Server Error`() = runTest {
        val dummyRequestBody = "dummy".toRequestBody("text/plain".toMediaTypeOrNull())
        val userNamePart = MultipartBody.Part.createFormData("userName", "John Doe", dummyRequestBody)
        val imagePart = MultipartBody.Part.createFormData("userImage", "img.jpg", dummyRequestBody)

        val errorJson = """{"code":500,"message":"Server failure"}""".trimIndent()
        val errorBody = errorJson.toResponseBody("application/json".toMediaTypeOrNull())

        coEvery { authApiService.profileUser(imagePart,userNamePart) } returns Response.error(500, errorBody)
        val result = source.profileUser(imagePart,userNamePart)

        assertTrue(result is EcommerceResponse.Failure)
        assertEquals(500, result.code)
        assertEquals("Server failure", result.error)
    }

    @Test
    fun `profileUser returns error when exception is thrown`() = runTest {
        val dummyRequestBody = "dummy".toRequestBody("text/plain".toMediaTypeOrNull())
        val userNamePart = MultipartBody.Part.createFormData("userName", "John Doe", dummyRequestBody)
        val imagePart = MultipartBody.Part.createFormData("userImage", "img.jpg", dummyRequestBody)

        coEvery { authApiService.profileUser(imagePart,userNamePart) } throws RuntimeException("Timeout")

        val result = source.profileUser(imagePart,userNamePart)

        assertTrue(result is EcommerceResponse.Failure)
        assertEquals("Timeout", result.error)
    }


}
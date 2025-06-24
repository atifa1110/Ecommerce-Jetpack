package com.example.core.data.data.network.datasource

import com.example.core.data.network.api.FirebaseApiService
import com.example.core.data.network.datasource.FirebaseNetworkDataSource
import com.example.core.data.network.request.TokenRequest
import com.example.core.data.network.response.BaseResponse
import com.example.core.data.network.response.EcommerceResponse
import com.google.gson.Gson
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class FirebaseNetworkDataSourceTest {

    private val firebaseApiService : FirebaseApiService = mockk()
    private val gson = Gson()

    private lateinit var source: FirebaseNetworkDataSource

    @Before
    fun setUp() {
        source = FirebaseNetworkDataSource(firebaseApiService, gson)
    }

    @Test
    fun `firebaseToken returns success on 200 api responds`() = runTest {
        val expectedData = BaseResponse(200,"Ok")
        val request = TokenRequest("token")
        coEvery {
            firebaseApiService.firebaseToken(request)
        } returns Response.success(expectedData)

        val result = source.updateFCMToken(request)

        assertTrue(result is EcommerceResponse.Success)
        assertEquals(expectedData.code,result.value.code)
        assertEquals(expectedData.message,result.value.message)
    }

    @Test
    fun `firebaseToken returns fails on 404 api responds`() = runTest {
        val errorJson = """{ "code": 404,"message": "Not Found"}""".trimIndent()
        val errorBody = errorJson.toResponseBody("application/json".toMediaTypeOrNull())

        val request = TokenRequest("token")
        coEvery {
            firebaseApiService.firebaseToken(request)
        } returns Response.error(404, errorBody)

        val result = source.updateFCMToken(request)

        assertTrue(result is EcommerceResponse.Failure)
        assertEquals(404,result.code)
    }

}
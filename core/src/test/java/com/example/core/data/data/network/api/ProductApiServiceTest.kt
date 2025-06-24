package com.example.core.data.data.network.api

import com.example.core.data.network.api.ProductApiService
import com.example.core.data.network.model.ProductDetailNetwork
import com.example.core.data.network.model.ProductNetwork
import com.example.core.data.network.model.ProductVariantNetwork
import com.example.core.data.network.model.ReviewNetwork
import com.example.core.data.network.response.DetailResponse
import com.example.core.data.network.response.ErrorResponse
import com.example.core.data.network.response.ProductResponse
import com.example.core.data.network.response.ReviewResponse
import com.example.core.data.network.response.SearchResponse
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
class ProductApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: ProductApiService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        val client = OkHttpClient.Builder().build()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ProductApiService::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `test list product filter success`() {
        mockWebServer.enqueueResponse("Products.json", 200)
        runBlocking {
            val actual = apiService.getProductFilter(
                "dell", "", 0, 0, "", 0, 0
            ).body()
            val expected = ProductResponse(
                200, "OK",
                ProductResponse.ProductPage(
                    10, 4, 1, 1,
                    arrayListOf(
                        ProductNetwork("1", "Product 1", 13849000, "images",
                            "Dell", "DellStore", 13, 5.0F
                        )
                    )
                )
            )

            assertEquals(expected, actual)
            assertEquals("Product 1", actual?.data?.items?.get(0)?.productName)
        }
    }

    @Test
    fun `test list product filter fails not found`() {
        mockWebServer.enqueueResponse("ProductsErrorNotFound.json", 404)
        runBlocking {
            val actual = apiService.getProductFilter(
                "dells", "", 0, 0, "", 0, 0
            )
            // ✅ Check status code and error state
            assertEquals(false, actual.isSuccessful)
            assertEquals(404, actual.code())

            // ✅ Optional: parse error body to check message
            val errorJson = actual.errorBody()?.string()
            println("Error Body: $errorJson") // Debugging

            // You can use a JSON parser to map this to ErrorResponse if needed
            // e.g., using Gson:
            val errorResponse = Gson().fromJson(errorJson, ErrorResponse::class.java)
            assertEquals("Not Found", errorResponse.message)
        }
    }

    @Test
    fun `test search product success`() {
        mockWebServer.enqueueResponse("Search.json", 200)
        runBlocking {
            val actual = apiService.searchProductList("lenovo").body()
            val expected = SearchResponse(
                200, "OK",
                arrayListOf(
                    "Lenovo Ideapad Gaming",
                    "Lenovo Ideapad Slim",
                    "Lenovo Ideapad Slim",
                    "Lenovo Legion Pro",
                    "Lenovo Legion Pro",
                    "Lenovo Yoga 6",
                    "Lenovo Yoga 7i",
                    "Lenovo Yoga 9i"
                )
            )

            assertEquals(expected, actual)
            assertEquals(8, actual?.data?.size)
        }
    }

    @Test
    fun `test search product empty success`() {
        mockWebServer.enqueueResponse("SearchEmpty.json", 200)
        runBlocking {
            val actual = apiService.searchProductList("lenovo").body()
            val expected = SearchResponse(
                200, "OK",
                arrayListOf()
            )

            assertEquals(expected, actual)
            assertEquals(0, actual?.data?.size)
        }
    }

    @Test
    fun `test detail product success`() {
        mockWebServer.enqueueResponse("Detail.json", 200)
        runBlocking {
            val actual = apiService.getProductDetail("b774d021-250a-4c3a-9c58-ab39edb36de5").body()
            val expected = DetailResponse(
                200,
                "OK",
                ProductDetailNetwork(
                    "b774d021-250a-4c3a-9c58-ab39edb36de5",
                    "Dell",
                    13849000,
                    listOf("image1", "image2", "image3"),
                    "Dell",
                    "DELL",
                    "DellStore",
                    13,
                    44,
                    10,
                    5,
                    100,
                    5.0F,
                    listOf(
                        ProductVariantNetwork("RAM 16GB", 0),
                        ProductVariantNetwork("RAM 32GB", 1000000)
                    )
                )
            )
            assertEquals(expected, actual)
            assertEquals("Dell", actual?.data?.productName)
        }
    }

    @Test
    fun `test detail product fails not found`() {
        mockWebServer.enqueueResponse("ProductsErrorNotFound.json", 404)
        runBlocking {
            val actual = apiService.getProductDetail("b774d021-250a-4c3a-9c58-ab39edb36de5")

            // ✅ Check status code and error state
            assertEquals(false, actual.isSuccessful)
            assertEquals(404, actual.code())

            // ✅ Optional: parse error body to check message
            val errorJson = actual.errorBody()?.string()
            println("Error Body: $errorJson") // Debugging

            // You can use a JSON parser to map this to ErrorResponse if needed
            // e.g., using Gson:
            val errorResponse = Gson().fromJson(errorJson, ErrorResponse::class.java)
            assertEquals("Not Found", errorResponse.message)
        }
    }

    @Test
    fun `test detail product fails null id`() {
        mockWebServer.enqueueResponse("ProductsErrorIdNull.json", 400)
        runBlocking {
            val actual = apiService.getProductDetail("b774d021-250a-4c3a-9c58-ab39edb36de5")

            // ✅ Check status code and error state
            assertEquals(false, actual.isSuccessful)
            assertEquals(400, actual.code())

            // ✅ Optional: parse error body to check message
            val errorJson = actual.errorBody()?.string()
            println("Error Body: $errorJson") // Debugging

            // You can use a JSON parser to map this to ErrorResponse if needed
            // e.g., using Gson:
            val errorResponse = Gson().fromJson(errorJson, ErrorResponse::class.java)
            assertEquals("productId cannot be null", errorResponse.message)
        }
    }

    @Test
    fun `test review product success`() {
        mockWebServer.enqueueResponse("Review.json", 200)
        runBlocking {
            val actual = apiService.getProductReview("b774d021-250a-4c3a-9c58-ab39edb36de5").body()
            val expected = ReviewResponse(
                200,
                "OK",
                listOf(
                    ReviewNetwork(
                        "John",
                        "image",
                        4,
                        "review"
                    ),
                    ReviewNetwork(
                        "Doe",
                        "image",
                        5,
                        "review"
                    )
                )
            )
            assertEquals(expected, actual)
            assertEquals(2, actual?.data?.size)
        }
    }

    @Test
    fun `test review product fails not found`() {
        mockWebServer.enqueueResponse("ProductsErrorNotFound.json", 404)
        runBlocking {
            val actual = apiService.getProductReview("b774d021-250a-4c3a-9c58-ab39edb36de5")


            // ✅ Check status code and error state
            assertEquals(false, actual.isSuccessful)
            assertEquals(404, actual.code())

            // ✅ Optional: parse error body to check message
            val errorJson = actual.errorBody()?.string()
            println("Error Body: $errorJson") // Debugging

            // You can use a JSON parser to map this to ErrorResponse if needed
            // e.g., using Gson:
            val errorResponse = Gson().fromJson(errorJson, ErrorResponse::class.java)
            assertEquals("Not Found", errorResponse.message)
        }
    }

    @Test
    fun `test review product fails null id`() {
        mockWebServer.enqueueResponse("ProductsErrorIdNull.json", 400)
        runBlocking {
            val actual = apiService.getProductReview("b774d021-250a-4c3a-9c58-ab39edb36de5")


            // ✅ Check status code and error state
            assertEquals(false, actual.isSuccessful)
            assertEquals(400, actual.code())

            // ✅ Optional: parse error body to check message
            val errorJson = actual.errorBody()?.string()
            println("Error Body: $errorJson") // Debugging

            // You can use a JSON parser to map this to ErrorResponse if needed
            // e.g., using Gson:
            val errorResponse = Gson().fromJson(errorJson, ErrorResponse::class.java)
            assertEquals("productId cannot be null", errorResponse.message)
        }
    }

}


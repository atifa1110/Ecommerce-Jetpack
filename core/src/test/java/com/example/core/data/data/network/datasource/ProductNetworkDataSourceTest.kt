package com.example.core.data.data.network.datasource

import com.example.core.data.network.api.ProductApiService
import com.example.core.data.network.datasource.ProductNetworkDataSource
import com.example.core.data.network.model.ProductDetailNetwork
import com.example.core.data.network.model.ProductNetwork
import com.example.core.data.network.model.ReviewNetwork
import com.example.core.data.network.response.DetailResponse
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.data.network.response.ProductResponse
import com.example.core.data.network.response.ReviewResponse
import com.example.core.data.network.response.SearchResponse
import com.google.gson.Gson
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
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
class ProductNetworkDataSourceTest {

    private val productApiService : ProductApiService = mockk()
    private val gson = Gson()

    private lateinit var source: ProductNetworkDataSource

    @Before
    fun setUp() {
        source = ProductNetworkDataSource(productApiService, gson)
    }

    private val data = ProductNetwork(
        productId = "1",
        productName = "Product",
        productPrice = 15000000,
        image = "image",
        brand = "apple",
        store = "apple store",
        sale = 4,
        productRating = 4.5F
    )

    private val detail = ProductDetailNetwork(
        productId = "1",
        productName = "Product",
        productPrice = 15000000,
        image = listOf("image"),
        brand = "apple",
        description = "description",
        store = "apple store",
        sale = 4,
        stock = 10,
        totalReview = 0,
        totalRating = 4,
        totalSatisfaction = 10,
        productRating = 4.5F
    )

    @Test
    fun `getProductFilter returns success on 200 api responds`() = runTest {
        val expectedData = ProductResponse(
            200,"Ok", ProductResponse.ProductPage(10,1,1,10, arrayListOf(data))
        )
        coEvery {
            productApiService.getProductFilter("laptop", "Lenovo", 5000, 20000, "price", 10, 1)
        } returns Response.success(expectedData)

        val result = source.getProductFilter("laptop", "Lenovo", 5000, 20000, "price", 10, 1)

        assertTrue(result is EcommerceResponse.Success)
        assertEquals(expectedData.code,result.value.code)
        assertEquals(expectedData.message,result.value.message)
        assertEquals(expectedData.data,result.value.data)
    }

    @Test
    fun `getProductFilter returns fails on 404 api responds`() = runTest {
        val errorJson = """{ "code": 404,"message": "Not Found"}""".trimIndent()
        val errorBody = errorJson.toResponseBody("application/json".toMediaTypeOrNull())

        coEvery {
            productApiService.getProductFilter("laptop", "Lenovo", 5000, 20000, "price", 10, 1)
        } returns Response.error(404, errorBody)

        val result = source.getProductFilter("laptop", "Lenovo", 5000, 20000, "price", 10, 1)

        assertTrue(result is EcommerceResponse.Failure)
        assertEquals(404,result.code)
        assertEquals("Not Found",result.error)
    }

    @Test
    fun `searchProduct returns success on 200 api responds`() = runTest {
        val expectedData = SearchResponse(
            200,"Ok", arrayListOf("Lenovo 1", "Lenovo 2", "Lenovo ")
        )
        coEvery { productApiService.searchProductList("lenovo")
        } returns Response.success(expectedData)

        val result = source.searchProductList("lenovo")

        assertTrue(result is EcommerceResponse.Success)
        assertEquals(expectedData.code,result.value.code)
        assertEquals(expectedData.message,result.value.message)
        assertEquals(expectedData.data,result.value.data)
    }

    @Test
    fun `searchProduct returns success empty on 200 api responds`() = runTest {
        val expectedData = SearchResponse(
            200,"Ok", arrayListOf()
        )
        coEvery { productApiService.searchProductList("lenovo")
        } returns Response.success(expectedData)

        val result = source.searchProductList("lenovo")

        assertTrue(result is EcommerceResponse.Success)
        assertEquals(expectedData.code,result.value.code)
        assertEquals(expectedData.message,result.value.message)
        assertTrue(result.value.data.isEmpty())
    }

    @Test
    fun `getProductDetail returns success on 200 api responds`() = runTest {
        val expectedData = DetailResponse(
            200,"Ok", detail
        )
        coEvery { productApiService.getProductDetail("1")
        } returns Response.success(expectedData)

        val result = source.getProductDetail("1")

        assertTrue(result is EcommerceResponse.Success)
        assertEquals(expectedData.code,result.value.code)
        assertEquals(expectedData.message,result.value.message)
        assertEquals(expectedData.data,result.value.data)
    }

    @Test
    fun `getProductDetail returns fails on 404 api responds`() = runTest {
        val errorJson = """{ "code": 404,"message": "Not Found"}""".trimIndent()
        val errorBody = errorJson.toResponseBody("application/json".toMediaTypeOrNull())

        coEvery { productApiService.getProductDetail("1")
        } returns Response.error(404, errorBody)

        val result = source.getProductDetail("1")

        assertTrue(result is EcommerceResponse.Failure)
        assertEquals(404,result.code)
        assertEquals("Not Found",result.error)

    }

    @Test
    fun `getProductReview returns success on 200 api responds`() = runTest {
        val expectedData = ReviewResponse(
            200,"Ok", listOf(ReviewNetwork("Test","image",0,"Good"))
        )
        coEvery { productApiService.getProductReview("1") } returns Response.success(expectedData)

        val result = source.getProductReview("1")

        assertTrue(result is EcommerceResponse.Success)
        assertEquals(expectedData.code,result.value.code)
        assertEquals(expectedData.message,result.value.message)
        assertEquals(expectedData.data,result.value.data)
    }

    @Test
    fun `getProductReview returns fails on 404 api responds`() = runTest {
        val errorJson = """{ "code": 404,"message": "Not Found"}""".trimIndent()
        val errorBody = errorJson.toResponseBody("application/json".toMediaTypeOrNull())

        coEvery { productApiService.getProductReview("1")
        } returns Response.error(404, errorBody)

        val result = source.getProductReview("1")

        assertTrue(result is EcommerceResponse.Failure)
        assertEquals(404,result.code)
        assertEquals("Not Found",result.error)
    }


}
package com.example.core.data.data.repository.product

import com.example.core.data.local.datasource.WishlistDatabaseSource
import com.example.core.data.network.datasource.ProductNetworkDataSource
import com.example.core.data.network.model.ProductDetailNetwork
import com.example.core.data.network.model.ReviewNetwork
import com.example.core.data.repository.product.ProductRepositoryImpl
import com.example.core.data.network.response.DetailResponse
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.data.network.response.ReviewResponse
import com.example.core.data.network.response.SearchResponse
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ProductRepositoryImplTest {

    var productNetworkDataSource: ProductNetworkDataSource = mockk()
    var wishlistDatabaseSource: WishlistDatabaseSource = mockk()
    private lateinit var repository: ProductRepositoryImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        repository = ProductRepositoryImpl(productNetworkDataSource, wishlistDatabaseSource)
    }

    @Test
    fun `searchProductList emits success`() = runTest {
        val expectedData = SearchResponse(200,"OK",arrayListOf("Laptop", "Phone"))
        coEvery { productNetworkDataSource.searchProductList("Lap")
        } returns EcommerceResponse.success(expectedData)

        val result = repository.searchProductList("Lap").toList()

        assertTrue(result[0] is EcommerceResponse.Loading)
        assertTrue(result[1] is EcommerceResponse.Success)
        assertEquals(expectedData.data, (result[1] as EcommerceResponse.Success).value)
    }

    @Test
    fun `searchProductList emits success empty`() = runTest {
        val expectedData = SearchResponse(200,"OK",arrayListOf())
        coEvery { productNetworkDataSource.searchProductList("Lap")
        } returns EcommerceResponse.success(expectedData)

        val result = repository.searchProductList("Lap").toList()

        assertTrue(result[0] is EcommerceResponse.Loading)
        assertTrue(result[1] is EcommerceResponse.Success)
        assertTrue((result[1] as EcommerceResponse.Success).value.isEmpty())
    }

    @Test
    fun `searchProductList emits failure when network fails`() = runTest {
        val query = "phone"
        val errorMessage = "Network error"

        coEvery { productNetworkDataSource.searchProductList(query) } returns
                EcommerceResponse.Failure(code = 500, error = errorMessage)

        val result = repository.searchProductList(query).toList()

        assertTrue(result[0] is EcommerceResponse.Loading)
        assertTrue(result[1] is EcommerceResponse.Failure)
        val failure = result[1] as EcommerceResponse.Failure
        assertEquals(500, failure.code)
        assertEquals(errorMessage, failure.error)
    }

    @Test
    fun `detailProduct emits success with isWishListed true`() = runTest {
        val expectedData = DetailResponse(
            200,"OK",ProductDetailNetwork(
                productId = "1", productName = "Product", productPrice = 15000000,
                image = listOf("image"), brand = "apple", description = "description", store = "apple store",
                sale = 4, stock = 10, totalReview = 0, totalRating = 4,
                totalSatisfaction = 10, productRating = 4.5F)
        )

        coEvery { productNetworkDataSource.getProductDetail("1") } returns
                EcommerceResponse.Success(expectedData)
        coEvery { wishlistDatabaseSource.isWishListed("1") } returns true

        val result = repository.detailProduct("1").toList()

        assertTrue(result[0] is EcommerceResponse.Loading)
        assertTrue(result[1] is EcommerceResponse.Success)
        assertEquals(expectedData.data.productId, (result[1] as EcommerceResponse.Success).value.productId)
    }

    @Test
    fun `detailProduct emits failure when network fails`() = runTest {
        val productId = "1"
        val errorMessage = "Not Found"

        coEvery { productNetworkDataSource.getProductDetail(productId) } returns
                EcommerceResponse.Failure(code = 404, error = errorMessage)

        val result = repository.detailProduct(productId).toList()

        assertTrue(result[0] is EcommerceResponse.Loading)
        assertTrue(result[1] is EcommerceResponse.Failure)
        val failure = result[1] as EcommerceResponse.Failure
        assertEquals(404, failure.code)
        assertEquals(errorMessage, failure.error)
    }

    @Test
    fun `getRatingProduct emits list of ReviewModel`() = runTest {
        val expectedData = ReviewResponse(200,"OK",listOf(
           ReviewNetwork("Test","image",0,"Good")
        ))

        coEvery { productNetworkDataSource.getProductReview("1") } returns
                EcommerceResponse.Success(expectedData)

        val result = repository.getRatingProduct("1").toList()

        assertTrue(result[0] is EcommerceResponse.Loading)
        assertTrue(result[1] is EcommerceResponse.Success)
        val emitted = (result[1] as EcommerceResponse.Success).value
        assertEquals(expectedData.data?.size, emitted.size)
    }

    @Test
    fun `getRatingProduct emits failure when network fails`() = runTest {
        val productId = "1"
        val errorMessage = "Server error"

        coEvery { productNetworkDataSource.getProductReview(productId) } returns
                EcommerceResponse.Failure(code = 500, error = errorMessage)

        val result = repository.getRatingProduct(productId).toList()

        assertTrue(result[0] is EcommerceResponse.Loading)
        assertTrue(result[1] is EcommerceResponse.Failure)
        val failure = result[1] as EcommerceResponse.Failure
        assertEquals(500, failure.code)
        assertEquals(errorMessage, failure.error)
    }

}
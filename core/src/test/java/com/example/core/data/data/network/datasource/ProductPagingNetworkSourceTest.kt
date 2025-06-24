package com.example.core.data.data.network.datasource

import androidx.paging.PagingSource
import com.example.core.data.network.datasource.ProductNetworkDataSource
import com.example.core.data.network.datasource.ProductPagingNetworkSource
import com.example.core.data.mapper.asProductModel
import com.example.core.data.network.model.ProductNetwork
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.data.network.response.PagingError
import com.example.core.data.network.response.ProductResponse
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ProductPagingNetworkSourceTest {

    private val dataSource: ProductNetworkDataSource = mockk()
    private lateinit var pagingSource: ProductPagingNetworkSource

    @Before
    fun setUp() {
        pagingSource = ProductPagingNetworkSource(
            dataSource = dataSource,
            search = "",
            brand = null,
            lowest = null,
            highest = null,
            sort = null
        )
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

    private val model = data.asProductModel()

    @Test
    fun `load returns Page when successful`() = runTest {
        val response = EcommerceResponse.Success(
            ProductResponse(
                code = 200,
                message = "OK",
                data = ProductResponse.ProductPage(
                    itemsPerPage = 10,
                    totalPages = 10,
                    currentItemCount = 1,
                    pageIndex = 1,
                    items = arrayListOf(data)
                )
            )
        )

        coEvery {
            dataSource.getProductFilter(any(), any(), any(), any(), any(), any(), any())
        } returns response

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = 1, loadSize = 10, placeholdersEnabled = false)
        )

        val expected = PagingSource.LoadResult.Page(
            data = listOf(model),
            prevKey = null,
            nextKey = 2
        )

        assertEquals(expected, result)
    }

    @Test
    fun `load returns error when 404`() = runTest {
        val error = EcommerceResponse.Failure(404, "Not Found")

        coEvery {
            dataSource.getProductFilter(any(), any(), any(), any(), any(), any(), any())
        } returns error

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = 1, loadSize = 10, placeholdersEnabled = false)
        )

        assertTrue(result is PagingSource.LoadResult.Error)
        assertTrue((result as PagingSource.LoadResult.Error).throwable is PagingError.NotFoundError)
    }

    @Test
    fun `load returns error when API error 400`() = runTest {
        val error = EcommerceResponse.Failure(400, "Bad Request")

        coEvery {
            dataSource.getProductFilter(any(), any(), any(), any(), any(), any(), any())
        } returns error

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = 1, loadSize = 10, placeholdersEnabled = false)
        )

        assertTrue(result is PagingSource.LoadResult.Error)
        assertTrue((result as PagingSource.LoadResult.Error).throwable is PagingError.ApiError)
    }

    @Test
    fun `load returns error on IOException`() = runTest {
        coEvery {
            dataSource.getProductFilter(any(), any(), any(), any(), any(), any(), any())
        } throws IOException()

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = 1, loadSize = 10, placeholdersEnabled = false)
        )

        assertTrue(result is PagingSource.LoadResult.Error)
        assertEquals(PagingError.ConnectionError, (result as PagingSource.LoadResult.Error).throwable)
    }

    @Test
    fun `load returns error on HttpException`() = runTest {
        val exception = HttpException(Response.error<Any>(500, "".toResponseBody()))
        coEvery {
            dataSource.getProductFilter(any(), any(), any(), any(), any(), any(), any())
        } throws exception

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = 1, loadSize = 10, placeholdersEnabled = false)
        )

        assertTrue(result is PagingSource.LoadResult.Error)
        val error = (result as PagingSource.LoadResult.Error).throwable
        assertTrue(error is PagingError.ApiError)
    }

}
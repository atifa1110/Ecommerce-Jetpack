package com.example.core.domain.usecase
import app.cash.turbine.test
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.domain.repository.product.ProductRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class GetSearchProductUseCaseTest {

    private lateinit var repository: ProductRepository
    private lateinit var useCase: GetSearchProductUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetSearchProductUseCase(repository)
    }

    @Test
    fun `invoke should return search result from repository`() = runTest {
        // Given
        val query = "laptop"
        val expectedList = listOf("laptop asus", "laptop acer")
        val expectedResponse = EcommerceResponse.Success(expectedList)

        coEvery { repository.searchProductList(query) } returns flowOf(expectedResponse)

        // When & Then
        useCase(query).test {
            val result = awaitItem()
            assertEquals(expectedResponse, result)
            awaitComplete()
        }
    }
}

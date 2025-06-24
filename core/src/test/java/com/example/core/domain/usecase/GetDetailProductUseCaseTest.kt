package com.example.core.domain.usecase

import app.cash.turbine.test
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.domain.model.ProductDetailModel
import com.example.core.domain.model.ProductVariantModel
import com.example.core.domain.repository.product.ProductRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class GetDetailProductUseCaseTest {

    private lateinit var repository: ProductRepository
    private lateinit var useCase: GetDetailProductUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetDetailProductUseCase(repository)
    }

    @Test
    fun `invoke should emit product detail when Success`() = runTest {
        // Given
        val id = "product_123"
        val dummyProduct = ProductDetailModel(
            productId = "p1",
            productName = "Sepatu Sneakers",
            productPrice = 250000,
            image = listOf("img1.jpg", "img2.jpg"),
            brand = "Adibas",
            description = "Keren dan nyaman",
            store = "Toko ABC",
            sale = 15,
            stock = 20,
            totalRating = 100,
            totalReview = 75,
            totalSatisfaction = 90,
            productRating = 4.6f,
            productVariant = listOf(ProductVariantModel("Size 42", 10000)),
            isWishlist = true
        )

        coEvery { repository.detailProduct(id) } returns flowOf(
            EcommerceResponse.Loading,
            EcommerceResponse.Success(dummyProduct)
        )
        // When & Then
        useCase(id).test {
            assertEquals(EcommerceResponse.Loading, awaitItem())
            assertEquals(EcommerceResponse.Success(dummyProduct), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `invoke should emit Error when repository fails`() = runTest {
        val id = "product_404"
        val expected = EcommerceResponse.Failure(400,"Product not found")

        coEvery { repository.detailProduct(id) } returns flowOf(
            EcommerceResponse.Loading,
            expected
        )

        useCase(id).test {
            assertEquals(EcommerceResponse.Loading, awaitItem())
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }
}

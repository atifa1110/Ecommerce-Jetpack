package com.example.core.domain.usecase

import app.cash.turbine.test
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.domain.model.WishlistModel
import com.example.core.domain.repository.wishlist.WishlistRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class GetWishlistUseCaseTest {

    private lateinit var repository: WishlistRepository
    private lateinit var useCase: GetWishlistUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetWishlistUseCase(repository)
    }

    @Test
    fun `invoke should return wishlist items`() = runTest {
        // Given
        val wishlist = listOf(
            WishlistModel(productId = "P001",
                productName = "T-Shirt",
                unitPrice = 100000,
                productImage = "https://example.com/tshirt.jpg",
                store = "Toko Baju",
                sale = 15,
                productRating = 4.6f,
                stock = 50,
                variantName = "L"),
            WishlistModel(productId = "P002",
                productName = "T-Shirt",
                unitPrice = 100000,
                productImage = "https://example.com/tshirt.jpg",
                store = "Toko Baju",
                sale = 15,
                productRating = 4.6f,
                stock = 50,
                variantName = "L")
        )
        val expected = EcommerceResponse.Success(wishlist)

        coEvery { repository.getWishlist() } returns flowOf(expected)

        // When
        val result = useCase()

        // Then
        result.test {
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }
}

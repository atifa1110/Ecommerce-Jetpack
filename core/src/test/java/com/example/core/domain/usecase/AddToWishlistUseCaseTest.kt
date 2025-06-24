package com.example.core.domain.usecase

import com.example.core.domain.model.WishlistModel
import com.example.core.domain.repository.wishlist.WishlistRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddToWishlistUseCaseTest {

    private lateinit var repository: WishlistRepository
    private lateinit var useCase: AddToWishlistUseCase

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        useCase = AddToWishlistUseCase(repository)
    }

    @Test
    fun `invoke should call addToWishlist with correct wishlistModel`() = runTest {
        // Given
        val wishlistModel = WishlistModel(
            productId = "P123",
            productName = "name",
            productImage = "image",
            unitPrice = 1000,
            variantName = "variant",
            store = "store",
            sale = 0,
            productRating = 2F,
            stock = 5
        )

        coEvery { repository.addToWishlist(wishlistModel) } returns Unit

        // When
        useCase(wishlistModel)

        // Then
        coVerify(exactly = 1) { repository.addToWishlist(wishlistModel) }
    }
}

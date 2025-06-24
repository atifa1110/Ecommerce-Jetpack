package com.example.core.domain.usecase

import com.example.core.domain.repository.wishlist.WishlistRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RemoveFromWishlistUseCaseTest {

    private lateinit var wishlistRepository: WishlistRepository
    private lateinit var useCase: RemoveFromWishlistUseCase

    @Before
    fun setUp() {
        wishlistRepository = mockk(relaxed = true)
        useCase = RemoveFromWishlistUseCase(wishlistRepository)
    }

    @Test
    fun `invoke should call removeFromWishlist with correct ID`() = runTest {
        // Given
        val wishlistId = "wishlist-id-123"

        // When
        useCase(wishlistId)

        // Then
        coVerify(exactly = 1) { wishlistRepository.removeFromWishlist(wishlistId) }
    }
}

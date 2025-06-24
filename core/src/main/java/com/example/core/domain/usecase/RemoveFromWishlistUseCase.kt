package com.example.core.domain.usecase

import com.example.core.domain.repository.wishlist.WishlistRepository
import javax.inject.Inject

class RemoveFromWishlistUseCase @Inject constructor(
    private val wishlistRepository: WishlistRepository
) {
    suspend operator fun invoke(id: String) {
        return wishlistRepository.removeFromWishlist(id)
    }
}
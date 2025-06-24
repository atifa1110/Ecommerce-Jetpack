package com.example.core.domain.usecase

import com.example.core.domain.model.WishlistModel
import com.example.core.domain.repository.wishlist.WishlistRepository
import javax.inject.Inject

class AddToWishlistUseCase @Inject constructor(
    private val wishlistRepository: WishlistRepository
) {
    suspend operator fun invoke(wishlistModel: WishlistModel) {
        return wishlistRepository.addToWishlist(wishlistModel)
    }
}
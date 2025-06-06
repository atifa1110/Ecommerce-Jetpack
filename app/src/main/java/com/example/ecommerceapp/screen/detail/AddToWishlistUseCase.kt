package com.example.ecommerceapp.screen.detail

import com.example.ecommerceapp.data.domain.WishlistModel
import com.example.ecommerceapp.repository.wishlist.WishlistRepository
import javax.inject.Inject

class AddToWishlistUseCase @Inject constructor(
    private val wishlistRepository: WishlistRepository
) {
    suspend operator fun invoke(wishlistModel: WishlistModel) {
        return wishlistRepository.addToWishlist(wishlistModel)
    }
}
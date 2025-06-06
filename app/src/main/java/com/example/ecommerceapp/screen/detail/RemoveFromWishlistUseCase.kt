package com.example.ecommerceapp.screen.detail

import com.example.ecommerceapp.data.domain.ProductDetailModel
import com.example.ecommerceapp.repository.wishlist.WishlistRepository
import javax.inject.Inject

class RemoveFromWishlistUseCase @Inject constructor(
    private val wishlistRepository: WishlistRepository
) {
    suspend operator fun invoke(id: String) {
        return wishlistRepository.removeFromWishlist(id)
    }
}
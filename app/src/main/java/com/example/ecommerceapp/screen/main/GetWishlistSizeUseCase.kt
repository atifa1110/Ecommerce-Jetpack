package com.example.ecommerceapp.screen.main

import com.example.ecommerceapp.repository.wishlist.WishlistRepository
import javax.inject.Inject

class GetWishlistSizeUseCase @Inject constructor(
    private val wishlistRepository: WishlistRepository
) {
    suspend operator fun invoke(): Int {
        return wishlistRepository.getWishlistSize()
    }
}

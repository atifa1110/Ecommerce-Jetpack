package com.example.ecommerceapp.screen.wishlist

import com.example.ecommerceapp.data.domain.WishlistModel
import com.example.ecommerceapp.data.network.response.EcommerceResponse
import com.example.ecommerceapp.repository.wishlist.WishlistRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWishlistUseCase @Inject constructor(
    private val wishlistRepository: WishlistRepository
) {
    operator fun invoke () : Flow<EcommerceResponse<List<WishlistModel>>>{
        return wishlistRepository.getWishlist()
    }
}
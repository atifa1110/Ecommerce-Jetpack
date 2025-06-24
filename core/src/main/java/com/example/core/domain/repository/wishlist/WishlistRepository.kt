package com.example.core.domain.repository.wishlist

import com.example.core.data.network.response.EcommerceResponse
import com.example.core.domain.model.WishlistModel
import kotlinx.coroutines.flow.Flow

interface WishlistRepository {
    suspend fun getWishlistSize() : Int
    fun getWishlist(): Flow<EcommerceResponse<List<WishlistModel>>>
    suspend fun addToWishlist(wishlistModel: WishlistModel)
    suspend fun removeFromWishlist(id: String)
}
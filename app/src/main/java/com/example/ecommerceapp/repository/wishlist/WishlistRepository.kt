package com.example.ecommerceapp.repository.wishlist

import com.example.ecommerceapp.data.domain.WishlistModel
import com.example.ecommerceapp.data.network.response.EcommerceResponse
import kotlinx.coroutines.flow.Flow

interface WishlistRepository {
    suspend fun getWishlistSize() : Int
    fun getWishlist(): Flow<EcommerceResponse<List<WishlistModel>>>
    suspend fun addToWishlist(wishlistModel: WishlistModel)
    suspend fun removeFromWishlist(id: String)
}
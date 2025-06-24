package com.example.core.data.repository.wishlist

import com.example.core.data.local.datasource.WishlistDatabaseSource
import com.example.core.data.local.room.entity.wishlist.WishlistEntity
import com.example.core.data.mapper.asWishlistModel
import com.example.core.data.mapper.listMap
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.domain.model.WishlistModel
import com.example.core.domain.repository.wishlist.WishlistRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WishlistRepositoryImpl @Inject constructor(
    private val wishlistDatabaseSource: WishlistDatabaseSource
) : WishlistRepository {

    override suspend fun getWishlistSize(): Int {
        val data = wishlistDatabaseSource.getWishlist().first()
        return if (data.isNotEmpty()) data.size else 0
    }

    override fun getWishlist(): Flow<EcommerceResponse<List<WishlistModel>>> {
        return flow {
            try {
                emit(EcommerceResponse.Loading)
                delay(1000L)
                val result =
                    wishlistDatabaseSource.getWishlist().listMap(WishlistEntity::asWishlistModel)
                        .first()
                emit(EcommerceResponse.Success(result))
            } catch (e: Exception) {
                emit(EcommerceResponse.Failure(code = -1, error = e.message ?: "Unknown error"))
            }
        }
    }

    override suspend fun addToWishlist(wishlistModel: WishlistModel) {
        return wishlistDatabaseSource.addToWishlist(wishlistModel)
    }

    override suspend fun removeFromWishlist(id: String) {
        return wishlistDatabaseSource.removeFromWishlist(id)
    }
}
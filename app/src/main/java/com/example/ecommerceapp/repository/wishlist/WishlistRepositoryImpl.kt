package com.example.ecommerceapp.repository.wishlist

import com.example.ecommerceapp.data.domain.WishlistModel
import com.example.ecommerceapp.data.domain.mapper.asCartModel
import com.example.ecommerceapp.data.domain.mapper.asWishlistModel
import com.example.ecommerceapp.data.domain.mapper.listMap
import com.example.ecommerceapp.data.local.database.entity.wishlist.WishlistEntity
import com.example.ecommerceapp.data.local.datasource.WishlistDatabaseSource
import com.example.ecommerceapp.data.network.response.EcommerceResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WishlistRepositoryImpl @Inject constructor(
    private val wishlistDatabaseSource: WishlistDatabaseSource
) : WishlistRepository{

    override suspend fun getWishlistSize(): Int {
        return wishlistDatabaseSource.getWishlist().listMap(WishlistEntity::asWishlistModel).first().size
    }
    override fun getWishlist(): Flow<EcommerceResponse<List<WishlistModel>>> {
        return flow {
            try {
                emit(EcommerceResponse.Loading)
                delay(2000L)
                val result = wishlistDatabaseSource.getWishlist().listMap(WishlistEntity::asWishlistModel).first()
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
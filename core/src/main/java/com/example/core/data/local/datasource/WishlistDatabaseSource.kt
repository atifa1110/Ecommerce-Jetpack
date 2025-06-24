package com.example.core.data.local.datasource

import com.example.core.data.local.room.dao.wishlist.WishlistDao
import com.example.core.data.mapper.asWishlistEntity
import com.example.core.domain.model.WishlistModel
import javax.inject.Inject

class WishlistDatabaseSource @Inject constructor(
    private val wishlistDao: WishlistDao
) {
    fun getWishlist() = wishlistDao.getByWishlist()
    suspend fun addToWishlist(wishlistModel: WishlistModel) =
        wishlistDao.insert(wishlistModel.asWishlistEntity())

    suspend fun removeFromWishlist(id: String) = wishlistDao.deleteById(id)
    suspend fun isWishListed(id: String) = wishlistDao.isWishListed(id)
}
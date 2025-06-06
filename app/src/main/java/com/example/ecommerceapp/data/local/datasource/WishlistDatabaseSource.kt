package com.example.ecommerceapp.data.local.datasource

import com.example.ecommerceapp.data.domain.WishlistModel
import com.example.ecommerceapp.data.domain.mapper.asWishlistEntity
import com.example.ecommerceapp.data.local.database.dao.wishlist.WishlistDao
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
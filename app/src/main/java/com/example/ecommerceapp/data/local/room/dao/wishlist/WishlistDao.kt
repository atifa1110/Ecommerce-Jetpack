package com.example.ecommerceapp.data.local.room.dao.wishlist

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ecommerceapp.data.local.room.Constants
import com.example.ecommerceapp.data.local.room.entity.wishlist.WishlistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WishlistDao {
    @Query("SELECT * FROM ${Constants.Tables.WISHLIST}")
    fun getByWishlist(): Flow<List<WishlistEntity>>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(wishlist: WishlistEntity)

    @Query("DELETE FROM ${Constants.Tables.WISHLIST} WHERE productId = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT EXISTS(SELECT * FROM ${Constants.Tables.WISHLIST} WHERE productId = :id)")
    suspend fun isWishListed(id: String): Boolean
}
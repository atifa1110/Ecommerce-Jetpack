package com.example.ecommerceapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ecommerceapp.data.local.database.dao.cart.CartDao
import com.example.ecommerceapp.data.local.database.dao.wishlist.WishlistDao
import com.example.ecommerceapp.data.local.database.entity.cart.CartEntity
import com.example.ecommerceapp.data.local.database.entity.wishlist.WishlistEntity

@Database(
    entities = [
        WishlistEntity::class,
        CartEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class EcommerceDatabase : RoomDatabase() {
    abstract fun wishlistDao(): WishlistDao
    abstract fun cartDao() : CartDao

    companion object {
        const val ECOMMERCE_DATABASE = "ecommerce.db"
    }
}
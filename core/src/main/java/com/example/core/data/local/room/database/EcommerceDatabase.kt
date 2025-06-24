package com.example.core.data.local.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.core.data.local.room.dao.cart.CartDao
import com.example.core.data.local.room.dao.notification.NotificationDao
import com.example.core.data.local.room.dao.wishlist.WishlistDao
import com.example.core.data.local.room.entity.cart.CartEntity
import com.example.core.data.local.room.entity.notification.NotificationEntity
import com.example.core.data.local.room.entity.wishlist.WishlistEntity

@Database(
    entities = [
        WishlistEntity::class,
        CartEntity::class,
        NotificationEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class EcommerceDatabase : RoomDatabase() {
    abstract fun wishlistDao(): WishlistDao
    abstract fun cartDao() : CartDao
    abstract fun notificationDao() : NotificationDao

    companion object {
        const val ECOMMERCE_DATABASE = "ecommerce.db"
    }
}
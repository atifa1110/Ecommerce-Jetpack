package com.example.ecommerceapp.di

import android.content.Context
import androidx.room.Room
import com.example.core.data.local.room.database.EcommerceDatabase
import com.example.core.data.local.room.dao.cart.CartDao
import com.example.core.data.local.room.dao.notification.NotificationDao
import com.example.core.data.local.room.dao.wishlist.WishlistDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): EcommerceDatabase {
        return Room.databaseBuilder(
                context.applicationContext,
                EcommerceDatabase::class.java,
                EcommerceDatabase.ECOMMERCE_DATABASE
            ).fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    fun provideNotificationDao(database: EcommerceDatabase): NotificationDao = database.notificationDao()


    @Provides
    fun provideWishlistDao(database: EcommerceDatabase): WishlistDao = database.wishlistDao()

    @Provides
    fun provideCartDao(database: EcommerceDatabase): CartDao = database.cartDao()

}

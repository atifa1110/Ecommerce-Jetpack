package com.example.ecommerceapp.di

import com.example.core.domain.repository.auth.AuthRepository
import com.example.core.data.repository.auth.AuthRepositoryImpl
import com.example.core.domain.repository.cart.CartRepository
import com.example.core.data.repository.cart.CartRepositoryImpl
import com.example.core.domain.repository.messaging.MessagingRepository
import com.example.core.data.repository.messaging.MessagingRepositoryImpl
import com.example.core.domain.repository.notification.NotificationRepository
import com.example.core.data.repository.notification.NotificationRepositoryImpl
import com.example.core.domain.repository.payment.PaymentRepository
import com.example.core.data.repository.payment.PaymentRepositoryImpl
import com.example.core.domain.repository.product.ProductRepository
import com.example.core.data.repository.product.ProductRepositoryImpl
import com.example.core.domain.repository.state.StateRepository
import com.example.core.data.repository.state.StateRepositoryImpl
import com.example.core.domain.repository.token.TokenRepository
import com.example.core.data.repository.token.TokenRepositoryImpl
import com.example.core.domain.repository.user.UserRepository
import com.example.core.data.repository.user.UserRepositoryImpl
import com.example.core.domain.repository.wishlist.WishlistRepository
import com.example.core.data.repository.wishlist.WishlistRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindMessagingRepository(repository: MessagingRepositoryImpl): MessagingRepository

    @Singleton
    @Binds
    abstract fun bindNotificationRepository(repository: NotificationRepositoryImpl): NotificationRepository

    @Singleton
    @Binds
    abstract fun bindPaymentRepository(repository: PaymentRepositoryImpl): PaymentRepository


    @Singleton
    @Binds
    abstract fun bindCartRepository(repository: CartRepositoryImpl): CartRepository


    @Singleton
    @Binds
    abstract fun bindWishlistRepository(repository: WishlistRepositoryImpl): WishlistRepository


    @Singleton
    @Binds
    abstract fun bindProductRepository(repository: ProductRepositoryImpl): ProductRepository


    @Singleton
    @Binds
    abstract fun bindUserRepository(repository: UserRepositoryImpl): UserRepository


    @Singleton
    @Binds
    abstract fun bindStateRepository(repository: StateRepositoryImpl): StateRepository


    @Singleton
    @Binds
    abstract fun bindTokenRepository(repository: TokenRepositoryImpl): TokenRepository

    @Singleton
    @Binds
    abstract fun bindAuthRepository(repository: AuthRepositoryImpl): AuthRepository

}
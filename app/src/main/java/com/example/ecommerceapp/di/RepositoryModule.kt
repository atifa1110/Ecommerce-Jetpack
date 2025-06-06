package com.example.ecommerceapp.di

import com.example.ecommerceapp.repository.auth.AuthRepository
import com.example.ecommerceapp.repository.auth.AuthRepositoryImpl
import com.example.ecommerceapp.repository.cart.CartRepository
import com.example.ecommerceapp.repository.cart.CartRepositoryImpl
import com.example.ecommerceapp.repository.payment.PaymentRepository
import com.example.ecommerceapp.repository.payment.PaymentRepositoryImpl
import com.example.ecommerceapp.repository.product.ProductRepository
import com.example.ecommerceapp.repository.product.ProductRepositoryImpl
import com.example.ecommerceapp.repository.state.StateRepository
import com.example.ecommerceapp.repository.state.StateRepositoryImpl
import com.example.ecommerceapp.repository.token.TokenRepository
import com.example.ecommerceapp.repository.token.TokenRepositoryImpl
import com.example.ecommerceapp.repository.user.UserRepository
import com.example.ecommerceapp.repository.user.UserRepositoryImpl
import com.example.ecommerceapp.repository.wishlist.WishlistRepository
import com.example.ecommerceapp.repository.wishlist.WishlistRepositoryImpl
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
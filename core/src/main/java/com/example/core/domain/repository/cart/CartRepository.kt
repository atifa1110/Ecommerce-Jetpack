package com.example.core.domain.repository.cart

import com.example.core.data.network.response.EcommerceResponse
import com.example.core.domain.model.CartModel
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun getCartSize() : Int
    fun getAllCart(): Flow<EcommerceResponse<List<CartModel>>>
    suspend fun addToCart(cartModel: CartModel)
    suspend fun removeFromCartById(id: String)
    suspend fun removeFromCart(cartModel: CartModel)
    suspend fun removeAllCart()
    suspend fun updateCartQuantity(id: String, quantity: Int)
}
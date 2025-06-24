package com.example.core.data.repository.cart

import com.example.core.domain.repository.cart.CartRepository
import com.example.core.domain.model.CartModel
import com.example.core.data.mapper.asCartModel
import com.example.core.data.mapper.listMap
import com.example.core.data.local.room.entity.cart.CartEntity
import com.example.core.data.local.datasource.CartDatabaseSource
import com.example.core.data.network.response.EcommerceResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartDatabaseSource: CartDatabaseSource
) : CartRepository {

    override suspend fun getCartSize(): Int {
        val data = cartDatabaseSource.getAllCart().first()
        return if (data.isNotEmpty()) data.size else 0
    }

    override fun getAllCart(): Flow<EcommerceResponse<List<CartModel>>> {
        return flow {
            try {
                emit(EcommerceResponse.Loading)
                delay(1000L)
                val result = cartDatabaseSource.getAllCart().listMap(CartEntity::asCartModel).first()
                emit(EcommerceResponse.Success(result))
            } catch (e: Exception) {
                emit(EcommerceResponse.Failure(code = -1, error = e.message ?: "Unknown error"))
            }
        }
    }

    override suspend fun addToCart(cartModel: CartModel) {
        return cartDatabaseSource.insertOrUpdateCart(cartModel)
    }

    override suspend fun removeFromCartById(id: String) {
        return cartDatabaseSource.deleteCartById(id)
    }

    override suspend fun removeFromCart(cartModel: CartModel) {
        return cartDatabaseSource.deleteCart(cartModel)
    }

    override suspend fun removeAllCart() {
        return cartDatabaseSource.clearAllCart()
    }

    override suspend fun updateCartQuantity(id: String, quantity: Int) {
        return cartDatabaseSource.updateCartQuantity(id, quantity)
    }

}
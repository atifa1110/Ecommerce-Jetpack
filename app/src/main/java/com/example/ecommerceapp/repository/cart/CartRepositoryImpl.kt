package com.example.ecommerceapp.repository.cart

import com.example.ecommerceapp.data.domain.CartModel
import com.example.ecommerceapp.data.domain.mapper.asCartModel
import com.example.ecommerceapp.data.domain.mapper.listMap
import com.example.ecommerceapp.data.local.database.entity.cart.CartEntity
import com.example.ecommerceapp.data.local.datasource.CartDatabaseSource
import com.example.ecommerceapp.data.network.response.EcommerceResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartDatabaseSource: CartDatabaseSource
) : CartRepository{

    override suspend fun getCartSize(): Int {
        return cartDatabaseSource.getAllCart().listMap(CartEntity::asCartModel).first().size
    }

    override fun getAllCart(): Flow<EcommerceResponse<List<CartModel>>> {
        return flow {
            try {
                emit(EcommerceResponse.Loading)
                delay(2000L)
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
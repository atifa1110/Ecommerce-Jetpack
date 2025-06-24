package com.example.core.data.local.datasource

import com.example.core.data.local.room.dao.cart.CartDao
import com.example.core.data.mapper.asCartEntity
import com.example.core.domain.model.CartModel
import javax.inject.Inject

class CartDatabaseSource @Inject constructor(
    private val cartDao: CartDao
) {
    fun getAllCart() = cartDao.getAllCart()
    suspend fun insertOrUpdateCart(cartModel: CartModel) = cartDao.insertOrUpdateCart(cartModel.asCartEntity())
    suspend fun deleteCart(cartModel: CartModel) = cartDao.deleteCart(cartModel.asCartEntity())
    suspend fun deleteCartById(id: String) = cartDao.deleteCartById(id)
    suspend fun clearAllCart() = cartDao.clearAllCart()
    suspend fun updateCartQuantity(id: String, quantity: Int) = cartDao.updateCartQuantity(id,quantity)
}
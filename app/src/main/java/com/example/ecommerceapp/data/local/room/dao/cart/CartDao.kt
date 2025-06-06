package com.example.ecommerceapp.data.local.room.dao.cart

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ecommerceapp.data.local.room.Constants
import com.example.ecommerceapp.data.local.room.entity.cart.CartEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM ${Constants.Tables.CART}")
    fun getAllCart(): Flow<List<CartEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateCart(item: CartEntity)

    @Query("UPDATE ${Constants.Tables.CART} SET quantity = :quantity WHERE productId = :id")
    suspend fun updateCartQuantity(id: String, quantity: Int)

    @Delete
    suspend fun deleteCart(item: CartEntity)

    @Query("DELETE FROM ${Constants.Tables.CART} WHERE productId = :productId")
    suspend fun deleteCartById(productId: String)

    @Query("DELETE FROM ${Constants.Tables.CART}")
    suspend fun clearAllCart()
}

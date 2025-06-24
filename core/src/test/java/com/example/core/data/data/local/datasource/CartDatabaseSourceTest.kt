package com.example.core.data.data.local.datasource

import com.example.core.data.local.datasource.CartDatabaseSource
import com.example.core.domain.model.CartModel
import com.example.core.data.local.room.dao.cart.CartDao
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.Test

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class CartDatabaseSourceTest {

    private lateinit var source: CartDatabaseSource
    private val cartDao: CartDao = mockk(relaxed = true)

    @Before
    fun setup() {
        source = CartDatabaseSource(cartDao)
    }

    private val model = CartModel(
        productId = "111",
        productName = "Cart",
        image = "cart.jpg",
        variantName = "RAM 16GB",
        unitPrice = 15000000,
        quantity = 2,
        stock = 50
    )

    @Test
    fun `insertOrUpdateCart calls dao with converted entity`() = runBlocking {
        source.insertOrUpdateCart(model)

        coVerify { cartDao.insertOrUpdateCart(match {
            it.productId == model.productId &&
            it.productName == model.productName &&
            it.image == model.image &&
            it.variantName == model.variantName &&
            it.unitPrice == model.unitPrice &&
            it.quantity == model.quantity &&
            it.stock == model.stock
        }) }
    }

    @Test
    fun `deleteCartById calls dao with correct id`() = runBlocking {
        source.insertOrUpdateCart(model)
        source.deleteCart(model)

        coVerify { cartDao.deleteCart(match {
            it.productId == model.productId &&
                    it.productName == model.productName &&
                    it.image == model.image &&
                    it.variantName == model.variantName &&
                    it.unitPrice == model.unitPrice &&
                    it.quantity == model.quantity &&
                    it.stock == model.stock
        })}
    }

    @Test
    fun `deleteCartById should call DAO with correct id`() = runBlocking {
        source.deleteCartById("1")
        coVerify { source.deleteCartById("1") }
    }


    @Test
    fun `clearAllCart should call DAO`() = runBlocking {
        source.clearAllCart()
        coVerify { source.clearAllCart() }
    }

    @Test
    fun `updateCartQuantity should call DAO with correct id and quantity`() = runBlocking {
        source.updateCartQuantity("1", 10)

        coVerify { source.updateCartQuantity("1", 10) }
    }

    @Test
    fun `getAllCart should call DAO`() {
        source.getAllCart()
        coVerify { source.getAllCart() }
    }
}

package com.example.core.data.data.local.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.core.data.local.room.dao.cart.CartDao
import com.example.core.data.local.room.database.EcommerceDatabase
import com.example.core.data.local.room.entity.cart.CartEntity
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CartDaoTest {

    private lateinit var database : EcommerceDatabase
    private lateinit var dao: CartDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database =
            Room.inMemoryDatabaseBuilder(context, EcommerceDatabase::class.java).allowMainThreadQueries()
                .build()
        dao = database.cartDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `test add to cart room success`() {
        runBlocking {
            val entity = CartEntity(
                "b774d021-250a-4c3a-9c58-ab39edb36de5",
                "Dell", "image1", "RAM 16GB",
                30000000, 1, 10)

            dao.insertOrUpdateCart(entity)

            // Retrieve get all data
            val result = dao.getAllCart().first()
            assertEquals(result.isNotEmpty(), true)
            assertEquals(1, result.size)
            assertEquals("Dell", result[0].productName)
        }
    }

    @Test
    fun `test update quantity room success`() {
        runBlocking {
            val entity = CartEntity(
                "b774d021-250a-4c3a-9c58-ab39edb36de5",
                "Dell", "image1", "RAM 16GB",
                30000000, 1, 10)

            dao.insertOrUpdateCart(entity)
            dao.updateCartQuantity("b774d021-250a-4c3a-9c58-ab39edb36de5", 5)

            // Retrieve get all data
            val result = dao.getAllCart().first()
            assertEquals(result.isNotEmpty(), true)
            assertEquals(1, result.size)
            assertEquals(5, result[0].quantity)
        }
    }

    @Test
    fun `test delete by cart object success`() {
        runBlocking {
            val entity = CartEntity(
                "b774d021-250a-4c3a-9c58-ab39edb36de5",
                "Dell", "image1", "RAM 16GB",
                30000000, 1, 10)

            dao.insertOrUpdateCart(entity)
            dao.deleteCart(entity)

            // Retrieve get all data
            val result = dao.getAllCart().first()
            assertEquals(result.isEmpty(), true)
            assertEquals(0, result.size)
        }
    }

    @Test
    fun `test delete by cart id success`() = runBlocking {
        val entity = CartEntity(
            "b774d021-250a-4c3a-9c58-ab39edb36de5",
            "Dell", "image1", "RAM 16GB",
            30000000, 1, 10)

        dao.insertOrUpdateCart(entity)
        dao.deleteCartById("b774d021-250a-4c3a-9c58-ab39edb36de5")
        val result = dao.getAllCart().first()
        assertEquals(result.isEmpty(), true)
        assertEquals(0, result.size)
    }

    @Test
    fun `test delete all cart success`() = runBlocking {
        val entity = CartEntity(
            "b774d021-250a-4c3a-9c58-ab39edb36de5",
            "Dell", "image1", "RAM 16GB",
            30000000, 1, 10)

        dao.insertOrUpdateCart(entity)
        dao.clearAllCart()
        val result = dao.getAllCart().first()
        assertEquals(result.isEmpty(), true)
        assertEquals(0, result.size)
    }

}

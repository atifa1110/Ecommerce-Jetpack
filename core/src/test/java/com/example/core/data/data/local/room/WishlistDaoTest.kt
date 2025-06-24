package com.example.core.data.data.local.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.core.data.local.room.dao.wishlist.WishlistDao
import com.example.core.data.local.room.database.EcommerceDatabase
import com.example.core.data.local.room.entity.wishlist.WishlistEntity
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class WishlistDaoTest {

    private lateinit var database: EcommerceDatabase
    private lateinit var dao: WishlistDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database =
            Room.inMemoryDatabaseBuilder(context, EcommerceDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        dao = database.wishlistDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `test add to wishlist room success`() {
        runBlocking {
            val entity = WishlistEntity(
                "b774d021-250a-4c3a-9c58-ab39edb36de5",
                "Dell", "image", 13849000, "RAM 16GB",
                "DellStore", 13, 5.0F, 7
            )

            dao.insert(entity)

            // Retrieve get all data
            val result = dao.getByWishlist().first()
            assertEquals(result.isNotEmpty(), true)
            assertEquals(1, result.size)
            assertEquals("Dell", result[0].productName)
        }
    }

    @Test
    fun `test get wishlist room success`() {
        runBlocking {
            val entity = WishlistEntity(
            "b774d021-250a-4c3a-9c58-ab39edb36de5",
            "Dell", "image", 13849000, "RAM 16GB",
            "DellStore", 13, 5.0F, 7)

            dao.insert(entity)

            // Retrieve get all data
            val result = dao.getByWishlist().first()
            assertEquals(result.isNotEmpty(), true)
            assertEquals(1, result.size)
            assertEquals(7, result[0].stock)
        }
    }

    @Test
    fun `test delete wishlist by id room success`() {
        runBlocking {
            val entity = WishlistEntity(
                "b774d021-250a-4c3a-9c58-ab39edb36de5",
                "Dell", "image", 13849000, "RAM 16GB",
                "DellStore", 13, 5.0F, 7)

            dao.insert(entity)
            dao.deleteById("b774d021-250a-4c3a-9c58-ab39edb36de5")

            // Retrieve get all data
            val result = dao.getByWishlist().first()
            assertEquals(result.isEmpty(), true)
            assertEquals(0, result.size)
        }
    }
}
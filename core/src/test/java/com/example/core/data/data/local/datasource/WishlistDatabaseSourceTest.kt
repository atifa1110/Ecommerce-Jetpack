package com.example.core.data.data.local.datasource

import com.example.core.data.local.datasource.WishlistDatabaseSource
import com.example.core.domain.model.WishlistModel
import com.example.core.data.mapper.asWishlistEntity
import com.example.core.data.local.room.dao.wishlist.WishlistDao
import com.example.core.data.local.room.entity.wishlist.WishlistEntity
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(JUnit4::class)
class WishlistDatabaseSourceTest {
    private lateinit var source: WishlistDatabaseSource
    private val wishlistDao: WishlistDao = mockk(relaxed = true)

    @Before
    fun setup() {
        source = WishlistDatabaseSource(wishlistDao)
    }

    private val model = WishlistModel(
        productId = "1",
        productName = "wishlist",
        productImage = "image",
        unitPrice = 30000000,
        variantName = "RAM 16GB",
        store = "apple store",
        sale = 1,
        productRating = 3.5f,
        stock = 5
    )

    @Test
    fun `getWishlist should call dao getByWishlist`() = runTest{
        source.addToWishlist(model)
        source.getWishlist()

        coVerify { wishlistDao.getByWishlist() }
    }

    @Test
    fun `addToWishlist should insert mapped entity into dao`() = runTest {
        val slot = slot<WishlistEntity>()
        source.addToWishlist(model)
        coVerify { wishlistDao.insert(capture(slot)) }

        val expected = model.asWishlistEntity()
        val actual = slot.captured

        assertEquals(expected, actual)
    }

    @Test
    fun `removeFromWishlist should call deleteById`() = runTest {
        source.addToWishlist(model)
        val idToDelete = "1"
        source.removeFromWishlist(idToDelete)
        coVerify(exactly = 1) {
            wishlistDao.deleteById(idToDelete)
        }
    }

    @Test
    fun `isWishListed should call dao isWishListed`() = runTest {
        source.addToWishlist(model)
        source.isWishListed("1")
        coVerify { wishlistDao.isWishListed("1") }
    }

}
package com.example.core.data.data.repository.wishlist

import com.example.core.domain.model.WishlistModel
import com.example.core.data.mapper.asWishlistEntity
import com.example.core.data.local.datasource.WishlistDatabaseSource
import com.example.core.data.repository.wishlist.WishlistRepositoryImpl
import com.example.core.data.network.response.EcommerceResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WishlistRepositoryImplTest {

    private val wishlistDatabaseSource: WishlistDatabaseSource = mockk(relaxed = true)
    private val repository = WishlistRepositoryImpl(wishlistDatabaseSource)

    private val wishlistModel =
        WishlistModel(
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
    private val wishlistEntity = wishlistModel.asWishlistEntity()

    @Test
    fun `getWishlistSize returns correct size when not empty`() = runTest {
        every { wishlistDatabaseSource.getWishlist() } returns flowOf(listOf(wishlistEntity))
        val size = repository.getWishlistSize()
        assertEquals(1, size)
    }

    @Test
    fun `getWishlistSize returns 0 when empty`() = runTest {
        every { wishlistDatabaseSource.getWishlist() } returns flowOf(emptyList())
        val size = repository.getWishlistSize()
        assertEquals(0, size)
    }

    @Test
    fun `getWishlist emits success with data`() = runTest {
        every { wishlistDatabaseSource.getWishlist() } returns flowOf(listOf(wishlistEntity))

        val result = repository.getWishlist().toList()

        assert(result[0] is EcommerceResponse.Loading)
        assert(result[1] is EcommerceResponse.Success)
        assertEquals(1, (result[1] as EcommerceResponse.Success).value.size)
    }

    @Test
    fun `getWishlist emits failure on exception`() = runTest {
        every { wishlistDatabaseSource.getWishlist() } throws RuntimeException("DB error")

        val result = repository.getWishlist().toList()

        assert(result[0] is EcommerceResponse.Loading)
        assert(result[1] is EcommerceResponse.Failure)
        assertEquals("DB error", (result[1] as EcommerceResponse.Failure).error)
    }

    @Test
    fun `addToWishlist delegates to data source`() = runTest {
        coEvery { wishlistDatabaseSource.addToWishlist(wishlistModel) } returns Unit
        repository.addToWishlist(wishlistModel)
        coVerify { wishlistDatabaseSource.addToWishlist(wishlistModel) }
    }

    @Test
    fun `removeFromWishlist delegates to data source`() = runTest {
        val id = "1"
        coEvery { wishlistDatabaseSource.removeFromWishlist(id) } returns Unit
        repository.removeFromWishlist(id)
        coVerify { wishlistDatabaseSource.removeFromWishlist(id) }
    }
}

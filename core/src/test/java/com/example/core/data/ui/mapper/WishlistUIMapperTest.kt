package com.example.core.data.ui.mapper

import com.example.core.ui.mapper.asWishlist
import com.example.core.ui.mapper.asWishlistModel
import com.example.core.domain.model.WishlistModel
import com.example.core.ui.model.ProductDetail
import kotlin.test.Test
import kotlin.test.assertEquals

class WishlistUIMapperTest {

    @Test
    fun `ProductDetail should map to WishlistModel correctly`() {
        val productDetail = ProductDetail(
            productId = "P001",
            productName = "T-Shirt",
            productPrice = 100_000,
            image = listOf("image.jpg"),
            brand = "Brand A",
            store = "Store A",
            sale = 10,
            productRating = 4.5f,
            stock = 50,
            description = "",
            productVariant = null
        )

        val unitPrice = 90_000
        val variantName = "Size M"

        val wishlistModel = productDetail.asWishlistModel(unitPrice, variantName)

        assertEquals("P001", wishlistModel.productId)
        assertEquals("T-Shirt", wishlistModel.productName)
        assertEquals(unitPrice, wishlistModel.unitPrice)
        assertEquals("image.jpg", wishlistModel.productImage)
        assertEquals("Store A", wishlistModel.store)
        assertEquals(10, wishlistModel.sale)
        assertEquals(4.5f, wishlistModel.productRating)
        assertEquals(50, wishlistModel.stock)
        assertEquals("Size M", wishlistModel.variantName)
    }

    @Test
    fun `WishlistModel should map to Wishlist correctly`() {
        val wishlistModel = WishlistModel(
            productId = "P001",
            productName = "T-Shirt",
            unitPrice = 90_000,
            productImage = "image.jpg",
            store = "Store A",
            sale = 10,
            productRating = 4.5f,
            stock = 50,
            variantName = "Size M"
        )

        val wishlist = wishlistModel.asWishlist()

        assertEquals(wishlistModel.productId, wishlist.productId)
        assertEquals(wishlistModel.productName, wishlist.productName)
        assertEquals(wishlistModel.unitPrice, wishlist.unitPrice)
        assertEquals(wishlistModel.productImage, wishlist.productImage)
        assertEquals(wishlistModel.store, wishlist.store)
        assertEquals(wishlistModel.sale, wishlist.sale)
        assertEquals(wishlistModel.productRating, wishlist.productRating)
        assertEquals(wishlistModel.stock, wishlist.stock)
        assertEquals(wishlistModel.variantName, wishlist.variantName)
    }
}
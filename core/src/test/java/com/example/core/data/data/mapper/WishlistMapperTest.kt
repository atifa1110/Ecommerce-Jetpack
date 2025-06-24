package com.example.core.data.data.mapper

import com.example.core.domain.model.WishlistModel
import com.example.core.data.local.room.entity.wishlist.WishlistEntity
import com.example.core.data.mapper.asWishlistEntity
import com.example.core.data.mapper.asWishlistModel
import kotlin.test.Test
import kotlin.test.assertEquals


class WishlistMapperTest {

    @Test
    fun `should map WishlistEntity to WishlistModel correctly`() {
        val entity = WishlistEntity(
            productId = "P001",
            productName = "T-Shirt",
            unitPrice = 100_000,
            productImage = "https://example.com/tshirt.jpg",
            store = "Toko Baju",
            sale = 15,
            productRating = 4.6f,
            stock = 50,
            variantName = "L"
        )

        val model = entity.asWishlistModel()

        assertEquals(entity.productId, model.productId)
        assertEquals(entity.productName, model.productName)
        assertEquals(entity.unitPrice, model.unitPrice)
        assertEquals(entity.productImage, model.productImage)
        assertEquals(entity.store, model.store)
        assertEquals(entity.sale, model.sale)
        assertEquals(entity.productRating, model.productRating)
        assertEquals(entity.stock, model.stock)
        assertEquals(entity.variantName, model.variantName)
    }

    @Test
    fun `should map WishlistModel to WishlistEntity correctly`() {
        val model = WishlistModel(
            productId = "P002",
            productName = "Sepatu",
            unitPrice = 300_000,
            productImage = "https://example.com/shoes.jpg",
            store = "Toko Sepatu",
            sale = 20,
            productRating = 4.9f,
            stock = 25,
            variantName = "42"
        )

        val entity = model.asWishlistEntity()

        assertEquals(model.productId, entity.productId)
        assertEquals(model.productName, entity.productName)
        assertEquals(model.unitPrice, entity.unitPrice)
        assertEquals(model.productImage, entity.productImage)
        assertEquals(model.store, entity.store)
        assertEquals(model.sale, entity.sale)
        assertEquals(model.productRating, entity.productRating)
        assertEquals(model.stock, entity.stock)
        assertEquals(model.variantName, entity.variantName)
    }
}

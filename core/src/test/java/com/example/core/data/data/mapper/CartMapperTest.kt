package com.example.core.data.data.mapper

import com.example.core.domain.model.CartModel
import com.example.core.data.local.room.entity.cart.CartEntity
import com.example.core.data.mapper.asCartEntity
import com.example.core.data.mapper.asCartModel
import kotlin.test.Test
import kotlin.test.assertEquals

class CartMapperTest {

    @Test
    fun `CartEntity should map correctly to CartModel`() {
        val entity = CartEntity(
            productId = "123",
            productName = "Product A",
            image = "image_url",
            variantName = "Red - L",
            stock = 50,
            unitPrice = 15000,
            quantity = 2
        )

        val model = entity.asCartModel()

        assertEquals(entity.productId, model.productId)
        assertEquals(entity.productName, model.productName)
        assertEquals(entity.image, model.image)
        assertEquals(entity.variantName, model.variantName)
        assertEquals(entity.stock, model.stock)
        assertEquals(entity.unitPrice, model.unitPrice)
        assertEquals(entity.quantity, model.quantity)
    }

    @Test
    fun `CartModel should map correctly to CartEntity`() {
        val model = CartModel(
            productId = "456",
            productName = "Product B",
            image = "another_image_url",
            variantName = "Blue - M",
            stock = 20,
            unitPrice = 25000,
            quantity = 1
        )

        val entity = model.asCartEntity()

        assertEquals(model.productId, entity.productId)
        assertEquals(model.productName, entity.productName)
        assertEquals(model.image, entity.image)
        assertEquals(model.variantName, entity.variantName)
        assertEquals(model.stock, entity.stock)
        assertEquals(model.unitPrice, entity.unitPrice)
        assertEquals(model.quantity, entity.quantity)
    }

    @Test
    fun `Entity to Model and back should preserve data`() {
        val originalEntity = CartEntity(
            productId = "789",
            productName = "Product C",
            image = "test_img",
            variantName = "Green - S",
            stock = 100,
            unitPrice = 9999,
            quantity = 3
        )

        val roundTripEntity = originalEntity.asCartModel().asCartEntity()

        assertEquals(originalEntity, roundTripEntity)
    }
}

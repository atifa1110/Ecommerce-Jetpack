package com.example.core.data.ui.mapper

import com.example.core.ui.mapper.asCart
import com.example.core.ui.mapper.asCartModel
import com.example.core.ui.mapper.asItemTransactionModel
import com.example.core.ui.model.Cart
import com.example.core.ui.model.ProductDetail
import com.example.core.ui.model.ProductVariant
import com.example.core.ui.model.Wishlist
import com.example.core.domain.model.CartModel
import kotlin.test.Test
import kotlin.test.assertEquals

class CartUiMapperTest {

    @Test
    fun `Cart to ItemTransactionModel should map correctly`() {
        val cart = Cart(
            productId = "P001",
            productName = "Shoes",
            productImage = "url",
            variantName = "Size 42",
            stock = 10,
            unitPrice = 200000,
            quantity = 2,
            isCheck = true
        )

        val result = cart.asItemTransactionModel()

        assertEquals(cart.productId, result.productId)
        assertEquals(cart.variantName, result.variantName)
        assertEquals(cart.quantity, result.quantity)
    }

    @Test
    fun `CartModel to Cart should map correctly`() {
        val model = CartModel(
            productId = "P002",
            productName = "T-Shirt",
            image = "image.jpg",
            variantName = "M",
            stock = 15,
            unitPrice = 150000,
            quantity = 1
        )

        val cart = model.asCart()

        assertEquals(model.productId, cart.productId)
        assertEquals(model.productName, cart.productName)
        assertEquals(model.image, cart.productImage)
        assertEquals(model.variantName, cart.variantName)
        assertEquals(model.stock, cart.stock)
        assertEquals(model.unitPrice, cart.unitPrice)
        assertEquals(model.quantity, cart.quantity)
        assertEquals(false, cart.isCheck) // default false
    }

    @Test
    fun `Wishlist to CartModel should map with injected price and variant`() {
        val wishlist = Wishlist(
            productId = "P003",
            productName = "Backpack",
            productImage = "backpack.png",
            store = "StoreX",
            sale = 10,
            productRating = 4.5f,
            stock = 8,
            variantName = "Standard",
            unitPrice = 10000000
        )

        val cartModel = wishlist.asCartModel(unitPrice = 300000, variantName = "Large")

        assertEquals(wishlist.productId, cartModel.productId)
        assertEquals(wishlist.productName, cartModel.productName)
        assertEquals(wishlist.productImage, cartModel.image)
        assertEquals(300000, cartModel.unitPrice)
        assertEquals("Large", cartModel.variantName)
        assertEquals(1, cartModel.quantity)
        assertEquals(wishlist.stock, cartModel.stock)
    }

    @Test
    fun `ProductDetail to CartModel should map correctly with selected variant`() {
        val detail = ProductDetail(
            productId = "P004",
            productName = "Laptop",
            productPrice = 10000000,
            image = listOf("laptop.png"),
            brand = "TechBrand",
            description = "Gaming laptop",
            store = "TechStore",
            sale = 5,
            stock = 20,
            totalRating = 100,
            totalReview = 50,
            totalSatisfaction = 95,
            productRating = 4.8f,
            productVariant = listOf(),
            isWishlist = false
        )

        val variant = ProductVariant(
            variantName = "16GB RAM",
            variantPrice = 500000
        )

        val cartModel = detail.asCartModel(variant)

        assertEquals("P004", cartModel.productId)
        assertEquals("Laptop", cartModel.productName)
        assertEquals(10500000, cartModel.unitPrice) // base + variant
        assertEquals("laptop.png", cartModel.image)
        assertEquals("16GB RAM", cartModel.variantName)
        assertEquals(1, cartModel.quantity)
        assertEquals(20, cartModel.stock)
    }

    @Test
    fun `ProductDetail to Cart should map correctly with selected variant`() {
        val detail = ProductDetail(
            productId = "P005",
            productName = "Keyboard",
            productPrice = 500000,
            image = listOf("keyboard.png"),
            brand = "KeyTech",
            description = "Mechanical",
            store = "KeyStore",
            sale = 10,
            stock = 12,
            totalRating = 50,
            totalReview = 25,
            totalSatisfaction = 90,
            productRating = 4.3f,
            productVariant = listOf(),
            isWishlist = true
        )

        val variant = ProductVariant(
            variantName = "RGB",
            variantPrice = 50000
        )

        val cart = detail.asCart(variant)

        assertEquals("P005", cart.productId)
        assertEquals("Keyboard", cart.productName)
        assertEquals(550000, cart.unitPrice)
        assertEquals("keyboard.png", cart.productImage)
        assertEquals("RGB", cart.variantName)
        assertEquals(1, cart.quantity)
        assertEquals(12, cart.stock)
    }
}

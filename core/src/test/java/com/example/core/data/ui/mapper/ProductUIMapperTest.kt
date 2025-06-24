package com.example.core.data.ui.mapper

import com.example.core.domain.model.ProductDetailModel
import com.example.core.domain.model.ProductModel
import com.example.core.domain.model.ProductVariantModel
import com.example.core.ui.mapper.asProduct
import com.example.core.ui.mapper.asProductDetail
import com.example.core.ui.mapper.asProductVariant
import junit.framework.TestCase.assertEquals
import kotlin.test.Test

class ProductUiMapperTest {

    @Test
    fun `ProductModel should map to Product correctly`() {
        val model = ProductModel(
            productId = "p1",
            productName = "Sepatu Sneakers",
            productPrice = 250000,
            image = "image.jpg",
            brand = "Adibas",
            store = "Toko ABC",
            sale = 10,
            productRating = 4.5f
        )

        val product = model.asProduct()

        assertEquals(model.productId, product.productId)
        assertEquals(model.productName, product.productName)
        assertEquals(model.productPrice, product.productPrice)
        assertEquals(model.image, product.image)
        assertEquals(model.brand, product.brand)
        assertEquals(model.store, product.store)
        assertEquals(model.sale, product.sale)
        assertEquals(model.productRating, product.productRating)
    }

    @Test
    fun `ProductVariantModel should map to ProductVariant correctly`() {
        val variantModel = ProductVariantModel(
            variantName = "Size 42",
            variantPrice = 10000
        )

        val variant = variantModel.asProductVariant()

        assertEquals(variantModel.variantName, variant.variantName)
        assertEquals(variantModel.variantPrice, variant.variantPrice)
    }

    @Test
    fun `ProductDetailModel should map to ProductDetail correctly`() {
        val detailModel = ProductDetailModel(
            productId = "p1",
            productName = "Sepatu Sneakers",
            productPrice = 250000,
            image = listOf("img1.jpg", "img2.jpg"),
            brand = "Adibas",
            description = "Keren dan nyaman",
            store = "Toko ABC",
            sale = 15,
            stock = 20,
            totalRating = 100,
            totalReview = 75,
            totalSatisfaction = 90,
            productRating = 4.6f,
            productVariant = listOf(ProductVariantModel("Size 42", 10000)),
            isWishlist = true
        )

        val detail = detailModel.asProductDetail()

        assertEquals(detailModel.productId, detail.productId)
        assertEquals(detailModel.productName, detail.productName)
        assertEquals(detailModel.productPrice, detail.productPrice)
        assertEquals(detailModel.image, detail.image)
        assertEquals(detailModel.brand, detail.brand)
        assertEquals(detailModel.description, detail.description)
        assertEquals(detailModel.store, detail.store)
        assertEquals(detailModel.sale, detail.sale)
        assertEquals(detailModel.stock, detail.stock)
        assertEquals(detailModel.totalRating, detail.totalRating)
        assertEquals(detailModel.totalReview, detail.totalReview)
        assertEquals(detailModel.totalSatisfaction, detail.totalSatisfaction)
        assertEquals(detailModel.productRating, detail.productRating)
        assertEquals(detailModel.isWishlist, detail.isWishlist)

        assertEquals(1, detail.productVariant?.size)
        assertEquals("Size 42", detail.productVariant?.first()?.variantName)
        assertEquals(10000, detail.productVariant?.first()?.variantPrice)
    }
}

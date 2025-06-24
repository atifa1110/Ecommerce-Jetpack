package com.example.core.data.data.mapper

import com.example.core.data.mapper.asProductDetailModel
import com.example.core.data.mapper.asProductModel
import com.example.core.data.mapper.asProductVariantModel
import com.example.core.data.network.model.ProductDetailNetwork
import com.example.core.data.network.model.ProductNetwork
import com.example.core.data.network.model.ProductVariantNetwork
import kotlin.test.Test
import kotlin.test.assertEquals


class ProductMapperTest {

    @Test
    fun `should map ProductNetwork to ProductModel with nulls safely`() {
        val network = ProductNetwork(
            productId = null,
            productName = null,
            productPrice = null,
            image = null,
            brand = null,
            store = null,
            sale = null,
            productRating = null
        )

        val model = network.asProductModel()

        assertEquals("", model.productId)
        assertEquals("", model.productName)
        assertEquals(0, model.productPrice)
        assertEquals("", model.image)
        assertEquals("", model.brand)
        assertEquals("", model.store)
        assertEquals(0, model.sale)
        assertEquals(0F, model.productRating)
    }

    @Test
    fun `should map ProductVariantNetwork to ProductVariantModel correctly`() {
        val variantNetwork = ProductVariantNetwork(
            variantName = "Ukuran L",
            variantPrice = 50000
        )

        val variantModel = variantNetwork.asProductVariantModel()

        assertEquals("Ukuran L", variantModel.variantName)
        assertEquals(50000, variantModel.variantPrice)
    }

    @Test
    fun `should map ProductDetailNetwork to ProductDetailModel correctly`() {
        val detailNetwork = ProductDetailNetwork(
            productId = "P001",
            productName = "Kaos Polos",
            productPrice = 75000,
            image = listOf("https://example.com/kaos.png"),
            brand = "BrandA",
            description = "Kaos bahan adem",
            store = "Toko Kaos",
            sale = 10,
            stock = 50,
            totalRating = 100,
            totalReview = 80,
            totalSatisfaction = 90,
            productRating = 4.5F,
            productVariant = listOf(
                ProductVariantNetwork("Ukuran M", 75000),
                ProductVariantNetwork("Ukuran L", 80000)
            )
        )

        val model = detailNetwork.asProductDetailModel(isWishlist = true)

        assertEquals("P001", model.productId)
        assertEquals("Kaos Polos", model.productName)
        assertEquals(75000, model.productPrice)
        assertEquals("https://example.com/kaos.png", model.image?.get(0))
        assertEquals("BrandA", model.brand)
        assertEquals("Kaos bahan adem", model.description)
        assertEquals("Toko Kaos", model.store)
        assertEquals(10, model.sale)
        assertEquals(50, model.stock)
        assertEquals(100, model.totalRating)
        assertEquals(80, model.totalReview)
        assertEquals(90, model.totalSatisfaction)
        assertEquals(4.5F, model.productRating)
        assertEquals(2, model.productVariant?.size)
        assertEquals(true, model.isWishlist)
    }

}
package com.example.core.ui.mapper

import com.example.core.domain.model.ProductDetailModel
import com.example.core.domain.model.ProductModel
import com.example.core.domain.model.ProductVariantModel
import com.example.core.ui.model.Product
import com.example.core.ui.model.ProductDetail
import com.example.core.ui.model.ProductVariant

fun ProductModel.asProduct() = Product(
    productId = productId,
    productName = productName,
    productPrice = productPrice,
    image = image,
    brand = brand,
    store = store,
    sale = sale,
    productRating = productRating
)

fun ProductVariantModel.asProductVariant() = ProductVariant(
    variantName = variantName,
    variantPrice = variantPrice
)

fun ProductDetailModel.asProductDetail() = ProductDetail(
    productId = productId,
    productName = productName,
    productPrice = productPrice,
    image = image,
    brand = brand,
    description = description,
    store = store,
    sale = sale,
    stock = stock,
    totalRating = totalRating,
    totalReview = totalReview,
    totalSatisfaction = totalSatisfaction,
    productRating = productRating,
    productVariant = productVariant?.map { it.asProductVariant() },
    isWishlist = isWishlist
)
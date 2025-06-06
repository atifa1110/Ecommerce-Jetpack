package com.example.ecommerceapp.data.ui.mapper

import com.example.ecommerceapp.data.domain.ProductDetailModel
import com.example.ecommerceapp.data.domain.ProductModel
import com.example.ecommerceapp.data.domain.ProductVariantModel
import com.example.ecommerceapp.data.ui.Product
import com.example.ecommerceapp.data.ui.ProductDetail
import com.example.ecommerceapp.data.ui.ProductVariant

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

fun ProductVariant.asProductVariantModel() = ProductVariantModel(
    variantName = variantName,
    variantPrice = variantPrice
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

fun ProductDetail.asProductDetailModel() = ProductDetailModel(
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
    productVariant = productVariant?.map { it.asProductVariantModel() },
    isWishlist = isWishlist
)
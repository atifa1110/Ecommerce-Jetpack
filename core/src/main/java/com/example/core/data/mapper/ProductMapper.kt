package com.example.core.data.mapper

import com.example.core.domain.model.ProductDetailModel
import com.example.core.domain.model.ProductModel
import com.example.core.domain.model.ProductVariantModel
import com.example.core.data.network.model.ProductDetailNetwork
import com.example.core.data.network.model.ProductNetwork
import com.example.core.data.network.model.ProductVariantNetwork

fun ProductNetwork.asProductModel() = ProductModel(
    productId = productId?:"",
    productName = productName?:"",
    productPrice = productPrice?:0,
    image = image?:"",
    brand = brand?:"",
    store = store?:"",
    sale = sale?:0,
    productRating = productRating?:0F
)

fun ProductVariantNetwork.asProductVariantModel() = ProductVariantModel(
    variantName = variantName,
    variantPrice = variantPrice
)

fun ProductDetailNetwork.asProductDetailModel(isWishlist : Boolean) = ProductDetailModel(
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
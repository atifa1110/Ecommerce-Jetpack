package com.example.ecommerceapp.data.domain.mapper

import com.example.ecommerceapp.data.domain.ProductDetailModel
import com.example.ecommerceapp.data.domain.ProductModel
import com.example.ecommerceapp.data.domain.ProductVariantModel
import com.example.ecommerceapp.data.network.model.ProductDetailNetwork
import com.example.ecommerceapp.data.network.model.ProductNetwork
import com.example.ecommerceapp.data.network.model.ProductVariantNetwork

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
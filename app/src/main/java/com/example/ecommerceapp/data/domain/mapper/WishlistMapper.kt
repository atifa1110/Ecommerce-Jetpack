package com.example.ecommerceapp.data.domain.mapper

import com.example.ecommerceapp.data.domain.WishlistModel
import com.example.ecommerceapp.data.local.room.entity.wishlist.WishlistEntity

fun WishlistEntity.asWishlistModel() = WishlistModel(
    productId = productId,
    productName = productName,
    unitPrice = unitPrice,
    productImage = productImage,
    store = store,
    sale = sale,
    productRating = productRating,
    stock = stock,
    variantName = variantName
)

fun WishlistModel.asWishlistEntity() = WishlistEntity(
    productId = productId,
    productName = productName,
    unitPrice = unitPrice,
    productImage = productImage,
    store = store,
    sale = sale,
    productRating = productRating,
    stock = stock,
    variantName = variantName
)
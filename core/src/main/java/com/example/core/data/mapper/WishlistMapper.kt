package com.example.core.data.mapper

import com.example.core.domain.model.WishlistModel
import com.example.core.data.local.room.entity.wishlist.WishlistEntity

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
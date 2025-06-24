package com.example.core.ui.mapper

import com.example.core.domain.model.WishlistModel
import com.example.core.ui.model.ProductDetail
import com.example.core.ui.model.Wishlist

fun ProductDetail.asWishlistModel(unitPrice: Int, variantName: String) = WishlistModel(
    productId = productId?:"",
    productName = productName?:"",
    unitPrice = unitPrice,
    productImage = image?.get(0)?:"",
    store =store?:"",
    sale = sale?:0,
    productRating = productRating?:0F,
    stock = stock?:0,
    variantName = variantName
)

fun WishlistModel.asWishlist() = Wishlist(
    productId = productId,
    productName = productName,
    unitPrice = unitPrice,
    productImage = productImage,
    store =store,
    sale = sale,
    productRating = productRating,
    stock = stock,
    variantName = variantName
)
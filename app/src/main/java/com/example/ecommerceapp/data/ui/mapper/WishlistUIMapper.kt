package com.example.ecommerceapp.data.ui.mapper

import com.example.ecommerceapp.data.domain.WishlistModel
import com.example.ecommerceapp.data.ui.ProductDetail
import com.example.ecommerceapp.data.ui.Wishlist

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
package com.example.core.ui.model

data class Wishlist(
    val productId: String,
    val productName: String,
    val productImage: String,
    val unitPrice: Int,
    val variantName : String,
    val store: String,
    val sale: Int,
    val productRating: Float,
    val stock : Int,
)
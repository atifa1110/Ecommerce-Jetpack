package com.example.core.ui.model

data class ProductDetail (
    val productId: String?="",
    val productName: String?="",
    val productPrice: Int?=0,
    val image: List<String>?=emptyList(),
    val brand: String?="",
    val description: String?="",
    val store: String?="",
    val sale: Int?=0,
    val stock: Int?=0,
    val totalRating: Int?=0,
    val totalReview: Int?=0,
    val totalSatisfaction: Int?=0,
    val productRating: Float?=0F,
    val productVariant: List<ProductVariant>? = emptyList(),
    val isWishlist : Boolean = false
)
package com.example.ecommerceapp.data.domain

data class CartModel(
    val productId: String,
    val productName: String,
    val image: String,
    val variantName: String,
    val unitPrice: Int,
    val quantity: Int,
    val stock: Int
)


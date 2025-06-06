package com.example.ecommerceapp.data.ui

data class Cart(
    val productId: String,
    val productName: String,
    val productImage: String,
    val variantName: String,
    val unitPrice: Int,
    val quantity: Int,
    val stock: Int,
    val isCheck: Boolean = false
) {
    val totalPrice: Int
        get() = unitPrice * quantity
}

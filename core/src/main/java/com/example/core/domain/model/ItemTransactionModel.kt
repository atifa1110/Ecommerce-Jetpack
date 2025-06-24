package com.example.core.domain.model

data class ItemTransactionModel (
    var productId: String,
    val variantName: String,
    val quantity: Int
)
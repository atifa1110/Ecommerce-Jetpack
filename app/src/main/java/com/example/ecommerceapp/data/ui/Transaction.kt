package com.example.ecommerceapp.data.ui

data class Transaction (
    val invoiceId: String,
    val status: Boolean,
    val date: String,
    val time: String,
    val payment: String,
    val total: Int,
    val items: List<ItemTransaction>,
    var rating: Int,
    var review: String,
    val image: String,
    val name: String
)
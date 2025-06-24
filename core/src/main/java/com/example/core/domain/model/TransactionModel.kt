package com.example.core.domain.model

data class TransactionModel (
    val invoiceId: String,
    val status: Boolean,
    val date: String,
    val time: String,
    val payment: String,
    val total: Int,
    val items: List<ItemTransactionModel>,
    var rating: Int?,
    var review: String?,
    val image: String,
    val name: String
)
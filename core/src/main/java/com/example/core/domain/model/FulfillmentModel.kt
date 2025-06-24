package com.example.core.domain.model

data class FulfillmentModel (
    val invoiceId: String,
    val status: Boolean,
    val date: String,
    val time: String,
    val payment: String,
    val total: Int
)
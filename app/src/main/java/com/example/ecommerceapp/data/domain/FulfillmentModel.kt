package com.example.ecommerceapp.data.domain

data class FulfillmentModel (
    val invoiceId: String,
    val status: Boolean,
    val date: String,
    val time: String,
    val payment: String,
    val total: Int
)
package com.example.ecommerceapp.data.ui

import java.io.Serializable

data class Payment(
    val title: String,
    val item: List<PaymentItem>
) : Serializable {
    data class PaymentItem(
        val label: String,
        val image: String,
        val status: Boolean
    ) : Serializable
}

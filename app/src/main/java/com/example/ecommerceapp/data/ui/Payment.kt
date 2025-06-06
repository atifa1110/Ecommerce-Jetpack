package com.example.ecommerceapp.data.ui

data class Payment(
    val title: String,
    val item: List<PaymentItem>
) {
    data class PaymentItem(
        val label: String,
        val image: String,
        val status: Boolean
    )
}

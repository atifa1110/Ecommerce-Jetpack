package com.example.ecommerceapp.data.network.model

data class PaymentNetwork(
    val title: String,
    val item: List<PaymentNetworkItem>
) {
    data class PaymentNetworkItem(
        val label: String,
        val image: String,
        val status: Boolean
    )
}
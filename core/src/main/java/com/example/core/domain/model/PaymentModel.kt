package com.example.core.domain.model

data class PaymentModel(
    val title: String,
    val item: List<PaymentModelItem>
) {
    data class PaymentModelItem(
        val label: String,
        val image: String,
        val status: Boolean
    )
}

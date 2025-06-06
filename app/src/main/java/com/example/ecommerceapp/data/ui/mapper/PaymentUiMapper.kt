package com.example.ecommerceapp.data.ui.mapper

import com.example.ecommerceapp.data.domain.PaymentModel
import com.example.ecommerceapp.data.ui.Payment

fun PaymentModel.PaymentModelItem.asPaymentItem() = Payment.PaymentItem(
    label = label,
    image = image,
    status = status
)

fun List<PaymentModel>.asPayment() = map{ it.asPayment() }
fun PaymentModel.asPayment() = Payment(
    title = title,
    item = item.map { it.asPaymentItem() }
)
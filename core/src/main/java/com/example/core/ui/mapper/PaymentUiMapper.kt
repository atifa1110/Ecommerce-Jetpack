package com.example.core.ui.mapper

import com.example.core.domain.model.PaymentModel
import com.example.core.ui.model.Payment

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
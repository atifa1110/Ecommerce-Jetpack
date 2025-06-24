package com.example.core.data.mapper

import com.example.core.domain.model.PaymentModel
import com.example.core.data.network.model.PaymentNetwork

fun PaymentNetwork.PaymentNetworkItem.asPaymentItemModel() = PaymentModel.PaymentModelItem(
    label = label,
    image = image,
    status = status
)

fun List<PaymentNetwork>.asPaymentModel() = map{ it.asPaymentModel() }
fun PaymentNetwork.asPaymentModel() = PaymentModel(
    title = title,
    item = item.map { it.asPaymentItemModel() }
)
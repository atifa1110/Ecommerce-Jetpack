package com.example.ecommerceapp.data.domain.mapper

import com.example.ecommerceapp.data.domain.PaymentModel
import com.example.ecommerceapp.data.network.model.PaymentNetwork

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
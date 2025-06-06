package com.example.ecommerceapp.data.ui.mapper

import com.example.ecommerceapp.data.domain.FulfillmentModel
import com.example.ecommerceapp.data.ui.Fulfillment

fun FulfillmentModel.asFulfillment() = Fulfillment(
    invoiceId = invoiceId,
    status = status,
    date = date,
    time = time,
    payment = payment,
    total = total,
)
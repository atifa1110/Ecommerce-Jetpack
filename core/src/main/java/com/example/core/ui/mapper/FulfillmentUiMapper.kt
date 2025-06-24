package com.example.core.ui.mapper

import com.example.core.domain.model.FulfillmentModel
import com.example.core.ui.model.Fulfillment

fun FulfillmentModel.asFulfillment() = Fulfillment(
    invoiceId = invoiceId,
    status = status,
    date = date,
    time = time,
    payment = payment,
    total = total,
)
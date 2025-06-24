package com.example.core.data.mapper

import com.example.core.domain.model.FulfillmentModel
import com.example.core.data.network.model.FulfillmentNetwork

fun FulfillmentNetwork.asFulfillmentModel() = FulfillmentModel(
    invoiceId = invoiceId?:"",
    status = status?:false,
    date = date?:"",
    time = time?:"",
    payment = payment?:"",
    total = total?:0
)
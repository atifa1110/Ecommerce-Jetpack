package com.example.ecommerceapp.data.domain.mapper

import com.example.ecommerceapp.data.domain.FulfillmentModel
import com.example.ecommerceapp.data.network.model.FulfillmentNetwork

fun FulfillmentNetwork.asFulfillmentModel() = FulfillmentModel(
    invoiceId = invoiceId?:"",
    status = status?:false,
    date = date?:"",
    time = time?:"",
    payment = payment?:"",
    total = total?:0
)
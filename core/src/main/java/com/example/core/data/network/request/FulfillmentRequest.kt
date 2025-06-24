package com.example.core.data.network.request

import com.example.core.data.network.model.ItemTransactionNetwork
import com.google.gson.annotations.SerializedName

data class FulfillmentRequest(
    @SerializedName("payment")
    val payment: String,
    @SerializedName("items")
    val items: List<ItemTransactionNetwork>
)

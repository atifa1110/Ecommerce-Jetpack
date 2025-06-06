package com.example.ecommerceapp.data.network.model

import com.google.gson.annotations.SerializedName

data class FulfillmentNetwork (
    @SerializedName("invoiceId")
    val invoiceId: String? = null,
    @SerializedName("status")
    val status: Boolean? = null,
    @SerializedName("date")
    val date: String? = null,
    @SerializedName("time")
    val time: String? = null,
    @SerializedName("payment")
    val payment: String? = null,
    @SerializedName("total")
    val total: Int? = null
)
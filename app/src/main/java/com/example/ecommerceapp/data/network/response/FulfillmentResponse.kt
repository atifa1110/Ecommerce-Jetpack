package com.example.ecommerceapp.data.network.response

import com.example.ecommerceapp.data.network.model.FulfillmentNetwork
import com.google.gson.annotations.SerializedName

data class FulfillmentResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("message")
    var message: String,
    @SerializedName("data")
    var data : FulfillmentNetwork
)
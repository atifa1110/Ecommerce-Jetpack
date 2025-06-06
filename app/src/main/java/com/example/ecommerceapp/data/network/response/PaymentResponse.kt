package com.example.ecommerceapp.data.network.response

import com.example.ecommerceapp.data.network.model.PaymentNetwork
import com.google.gson.annotations.SerializedName

data class PaymentResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("message")
    var message: String,
    @SerializedName("data")
    var data : List<PaymentNetwork>
)
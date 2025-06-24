package com.example.core.data.network.response

import com.example.core.data.network.model.PaymentNetwork
import com.google.gson.annotations.SerializedName

data class PaymentResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("message")
    var message: String,
    @SerializedName("data")
    var data : List<PaymentNetwork>
)
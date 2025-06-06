package com.example.ecommerceapp.data.network.response

import com.example.ecommerceapp.data.network.model.ReviewNetwork
import com.google.gson.annotations.SerializedName

data class ReviewResponse (
    @SerializedName("code")
    var code: Int,
    @SerializedName("message")
    var message: String,
    @SerializedName("data")
    var data : List<ReviewNetwork>?
)
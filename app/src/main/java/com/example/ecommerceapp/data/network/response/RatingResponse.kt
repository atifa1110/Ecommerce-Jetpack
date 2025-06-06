package com.example.ecommerceapp.data.network.response

import com.google.gson.annotations.SerializedName

class RatingResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("message")
    var message: String,
)
package com.example.core.data.network.response

import com.google.gson.annotations.SerializedName

data class BaseResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("message")
    var message: String
)
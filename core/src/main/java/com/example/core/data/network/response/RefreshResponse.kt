package com.example.core.data.network.response

import com.example.core.data.network.model.TokenNetwork
import com.google.gson.annotations.SerializedName

data class RefreshResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("message")
    var message: String,
    @SerializedName("data")
    var data: TokenNetwork
)
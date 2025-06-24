package com.example.core.data.network.request

import com.google.gson.annotations.SerializedName

data class TokenRequest(
    @SerializedName("token")
    val token: String
)
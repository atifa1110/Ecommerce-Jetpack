package com.example.core.data.network.response

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String
)
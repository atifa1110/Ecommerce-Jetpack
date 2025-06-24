package com.example.core.data.network.model

import com.google.gson.annotations.SerializedName

data class TokenNetwork(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String,
    @SerializedName("expiresAt")
    val expiresAt: Long
)

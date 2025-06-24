package com.example.core.data.network.model

import com.google.gson.annotations.SerializedName

data class LoginNetwork(
    @SerializedName("userName")
    var userName: String,
    @SerializedName("userImage")
    var userImage: String,
    @SerializedName("accessToken")
    var accessToken: String,
    @SerializedName("refreshToken")
    var refreshToken: String,
    @SerializedName("expiresAt")
    var expiresAt: Long
)
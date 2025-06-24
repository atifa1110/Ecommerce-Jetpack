package com.example.core.data.network.model

import com.google.gson.annotations.SerializedName

data class UserNetwork(
    @SerializedName("userName")
    var userName: String,
    @SerializedName("userImage")
    var userImage: String
)
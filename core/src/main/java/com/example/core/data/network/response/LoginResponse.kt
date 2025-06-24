package com.example.core.data.network.response

import com.example.core.data.network.model.LoginNetwork
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("message")
    var message: String,
    @SerializedName("data")
    var data : LoginNetwork
)
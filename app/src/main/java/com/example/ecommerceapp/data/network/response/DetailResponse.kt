package com.example.ecommerceapp.data.network.response

import com.example.ecommerceapp.data.network.model.ProductDetailNetwork
import com.google.gson.annotations.SerializedName

data class DetailResponse (
    @SerializedName("code")
    var code: Int,
    @SerializedName("message")
    var message: String,
    @SerializedName("data")
    var data : ProductDetailNetwork
)
package com.example.core.data.network.model

import com.google.gson.annotations.SerializedName

data class ItemTransactionNetwork(
    @SerializedName("productId")
    var productId: String,
    @SerializedName("variantName")
    val variantName: String,
    @SerializedName("quantity")
    val quantity: Int
)
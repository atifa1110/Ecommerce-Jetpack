package com.example.ecommerceapp.data.network.model

import com.google.gson.annotations.SerializedName

data class ProductVariantNetwork(
    @SerializedName("variantName")
    val variantName: String,
    @SerializedName("variantPrice")
    val variantPrice: Int
)
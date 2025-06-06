package com.example.ecommerceapp.data.network.model

import com.google.gson.annotations.SerializedName

data class ProductNetwork(
    @SerializedName("productId")
    val productId : String? = "",
    @SerializedName("productName")
    val productName : String? = "",
    @SerializedName("productPrice")
    val productPrice : Int? = 0,
    @SerializedName("image")
    val image : String? = "",
    @SerializedName("brand")
    val brand : String? = "",
    @SerializedName("store")
    val store : String? = "",
    @SerializedName("sale")
    val sale : Int? = 0,
    @SerializedName("productRating")
    val productRating : Float? = 0F
)
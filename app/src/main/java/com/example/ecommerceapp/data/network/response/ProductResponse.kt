package com.example.ecommerceapp.data.network.response

import com.example.ecommerceapp.data.network.model.ProductNetwork
import com.google.gson.annotations.SerializedName

data class ProductResponse (
    @SerializedName("code")
    var code: Int,
    @SerializedName("message")
    var message: String,
    @SerializedName("data")
    var data : ProductPage
){
    data class ProductPage(
        @SerializedName("itemsPerPage")
        val itemsPerPage: Int,
        @SerializedName("currentItemCount")
        val currentItemCount: Int,
        @SerializedName("pageIndex")
        val pageIndex: Int,
        @SerializedName("totalPages")
        val totalPages: Int,
        @SerializedName("items")
        val items: ArrayList<ProductNetwork>
    )
}
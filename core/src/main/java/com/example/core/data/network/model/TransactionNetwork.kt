package com.example.core.data.network.model

import com.google.gson.annotations.SerializedName

data class TransactionNetwork (
    @SerializedName("invoiceId")
    val invoiceId: String,
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("date")
    val date: String,
    @SerializedName("time")
    val time: String,
    @SerializedName("payment")
    val payment: String,
    @SerializedName("total")
    val total: Int,
    @SerializedName("items")
    val items: List<ItemTransactionNetwork>,
    @SerializedName("rating")
    var rating: Int?,
    @SerializedName("review")
    var review: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("name")
    val name: String
)
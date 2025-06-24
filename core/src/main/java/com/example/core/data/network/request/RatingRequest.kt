package com.example.core.data.network.request

data class RatingRequest(
    val invoiceId : String,
    val rating : Int,
    val review : String
)

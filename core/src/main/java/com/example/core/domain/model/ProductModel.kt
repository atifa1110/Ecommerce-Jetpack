package com.example.core.domain.model

data class ProductModel (
    val productId : String,
    val productName : String,
    val productPrice : Int,
    val image : String,
    val brand : String,
    val store : String,
    val sale : Int,
    val productRating : Float
)
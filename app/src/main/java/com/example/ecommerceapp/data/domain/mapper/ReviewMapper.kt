package com.example.ecommerceapp.data.domain.mapper

import com.example.ecommerceapp.data.domain.ReviewModel
import com.example.ecommerceapp.data.network.model.ReviewNetwork

fun ReviewNetwork.asReviewModel() = ReviewModel(
    userName = userName?:"",
    userImage = userImage?:"",
    userRating = userRating?:0,
    userReview = userReview?:""
)
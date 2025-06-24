package com.example.core.data.mapper

import com.example.core.domain.model.ReviewModel
import com.example.core.data.network.model.ReviewNetwork

fun ReviewNetwork.asReviewModel() = ReviewModel(
    userName = userName?:"",
    userImage = userImage?:"",
    userRating = userRating?:0,
    userReview = userReview?:""
)
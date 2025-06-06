package com.example.ecommerceapp.data.ui.mapper

import com.example.ecommerceapp.data.domain.ReviewModel
import com.example.ecommerceapp.data.ui.Review

fun ReviewModel.asReview() = Review(
    userName = userName,
    userImage = userImage,
    userReview = userReview,
    userRating = userRating
)
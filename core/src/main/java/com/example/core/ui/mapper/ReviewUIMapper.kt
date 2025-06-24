package com.example.core.ui.mapper

import com.example.core.domain.model.ReviewModel
import com.example.core.ui.model.Review

fun ReviewModel.asReview() = Review(
    userName = userName,
    userImage = userImage,
    userReview = userReview,
    userRating = userRating
)
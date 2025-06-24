package com.example.core.data.network.model

import com.google.gson.annotations.SerializedName

data class ReviewNetwork(
    @SerializedName("userName")
    var userName: String?=null,
    @SerializedName("userImage")
    var userImage: String?=null,
    @SerializedName("userRating")
    var userRating: Int?=null,
    @SerializedName("userReview")
    var userReview: String?=null,
)
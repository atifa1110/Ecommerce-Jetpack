package com.example.core.data.ui.mapper

import com.example.core.ui.mapper.asReview
import com.example.core.domain.model.ReviewModel
import kotlin.test.Test
import kotlin.test.assertEquals

class ReviewUIMapperTest {
    @Test
    fun `ReviewModel should map to Review correctly`() {
        val model = ReviewModel(
            userName = "Atifa",
            userImage = "profile.jpg",
            userReview = "Produk sangat bagus!",
            userRating = 5
        )

        val result = model.asReview()

        assertEquals(model.userName, result.userName)
        assertEquals(model.userImage, result.userImage)
        assertEquals(model.userReview, result.userReview)
        assertEquals(model.userRating, result.userRating)
    }
}
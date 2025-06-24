package com.example.core.data.data.mapper

import com.example.core.data.mapper.asReviewModel
import com.example.core.data.network.model.ReviewNetwork
import kotlin.test.Test
import kotlin.test.assertEquals

class ReviewMapperTest {

    @Test
    fun `should map ReviewNetwork to ReviewModel correctly with non-null values`() {
        val network = ReviewNetwork(
            userName = "Adit",
            userImage = "https://example.com/image.png",
            userRating = 5,
            userReview = "Produk bagus banget!"
        )

        val model = network.asReviewModel()

        assertEquals("Adit", model.userName)
        assertEquals("https://example.com/image.png", model.userImage)
        assertEquals(5, model.userRating)
        assertEquals("Produk bagus banget!", model.userReview)
    }

    @Test
    fun `should map ReviewNetwork to ReviewModel with null values using default`() {
        val network = ReviewNetwork(
            userName = null,
            userImage = null,
            userRating = null,
            userReview = null
        )

        val model = network.asReviewModel()

        assertEquals("", model.userName)
        assertEquals("", model.userImage)
        assertEquals(0, model.userRating)
        assertEquals("", model.userReview)
    }
}

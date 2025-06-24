package com.example.core.domain.usecase

import app.cash.turbine.test
import com.example.core.domain.model.ReviewModel
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.domain.repository.product.ProductRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class GetReviewProductUseCaseTest {

    private lateinit var repository: ProductRepository
    private lateinit var useCase: GetReviewProductUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetReviewProductUseCase(repository)
    }

    @Test
    fun `invoke should return list of ReviewModel`() = runTest {
        // Arrange
        val reviewList = listOf(
            ReviewModel(userName = "John", userImage = "image", userRating = 5, userReview = "Great!"),
            ReviewModel(userName = "Jane",  userImage = "image", userRating = 4, userReview = "Good!")
        )
        val response = EcommerceResponse.Success(reviewList)
        every { repository.getRatingProduct("123") } returns flowOf(response)

        // Act & Assert
        useCase("123").test {
            val result = awaitItem()
            assertEquals(response, result)
            awaitComplete()
        }
    }
}

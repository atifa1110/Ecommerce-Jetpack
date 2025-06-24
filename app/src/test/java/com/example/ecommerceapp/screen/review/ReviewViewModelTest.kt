package com.example.ecommerceapp.screen.review

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.core.domain.model.ReviewModel
import com.example.core.data.network.response.EcommerceResponse
import com.example.ecommerceapp.graph.ReviewDestination
import com.example.core.domain.usecase.GetReviewProductUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ReviewViewModelTest {

    private val getReviewProductUseCase: GetReviewProductUseCase = mockk()
    private lateinit var viewModel: ReviewViewModel

    private val testProductId = "product-123"

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        val savedStateHandle = SavedStateHandle(mapOf("id" to testProductId))

        // 👇 Mock the object and method
        mockkObject(ReviewDestination)
        every { ReviewDestination.fromSavedStateHandle(any()) } returns testProductId

        viewModel = ReviewViewModel(getReviewProductUseCase,savedStateHandle)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadReviewProduct emits Success and updates state`() = runTest {
        val review = listOf(ReviewModel("name","image",4,"review"))
        val flow = flow{
            emit(EcommerceResponse.Loading)
            delay(100)
            emit(EcommerceResponse.Success(review))
        }

        coEvery { getReviewProductUseCase.invoke(testProductId) } returns flow

        viewModel.getReviewProduct()
        advanceTimeBy(50)

        viewModel.uiState.test {
            val loading = awaitItem()
            assertTrue(loading.isLoading)
            assertFalse(loading.isError)

            val state = awaitItem()
            assertFalse(state.isError)
            assertFalse(state.isLoading)
            assertEquals("name",state.reviews[0].userName)
        }

    }

    @Test
    fun `loadReviewProduct emits failure and updates state`() = runTest {
        val exception = "Network error"
        val flow = flow {
            emit(EcommerceResponse.Loading)
            delay(100)
            emit(EcommerceResponse.Failure(400, exception))
        }

        coEvery { getReviewProductUseCase.invoke(testProductId) } returns flow

        viewModel.getReviewProduct()
        advanceTimeBy(50)

        viewModel.uiState.test {
            val loading = awaitItem()
            assertTrue(loading.isLoading)
            assertFalse(loading.isError)

            val state = awaitItem()
            assertTrue(state.isError)
            assertFalse(state.isLoading)
        }

    }

}
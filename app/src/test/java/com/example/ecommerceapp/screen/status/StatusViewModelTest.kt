package com.example.ecommerceapp.screen.status

import app.cash.turbine.test
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.ui.model.Fulfillment
import com.example.ecommerceapp.firebase.StatusAnalytics
import com.example.core.domain.usecase.SetRatingTransactionUseCase
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertFalse

@OptIn(ExperimentalCoroutinesApi::class)
class StatusViewModelTest {

    private lateinit var viewModel: StatusViewModel
    private lateinit var setRatingTransactionUseCase: SetRatingTransactionUseCase
    private lateinit var statusAnalytics: StatusAnalytics

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        setRatingTransactionUseCase = mockk()
        statusAnalytics = mockk(relaxed = true)
        viewModel = StatusViewModel(setRatingTransactionUseCase, statusAnalytics)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onRatingChanged updates state`() = runTest {
        viewModel.onRatingChanged(4)
        assertEquals(4, viewModel.uiState.value.rating)
    }

    @Test
    fun `onReviewChanged updates state`() = runTest {
        viewModel.onReviewChanged("Great product")
        assertEquals("Great product", viewModel.uiState.value.review)
    }

    @Test
    fun `setRatingTransaction with invalid input emits error snackbar`() = runTest {
        val fulfillment = Fulfillment(
            invoiceId = "1", status = true,
            date = "date",time= "time", payment = "BCA",
            total = 10000000)

        val job = launch {
            viewModel.eventFlow.test {
                assertEquals(
                    StatusEvent.ShowSnackbar("Please enter a rating and review."),
                    awaitItem()
                )
                cancel() // end collection after receiving the event
            }
        }
        viewModel.onReviewChanged("")
        viewModel.onRatingChanged(0)
        viewModel.setRatingTransaction(fulfillment)
        advanceUntilIdle()
        job.join()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.isError)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `setRatingTransaction success flow updates state and emits success snackbar`() = runTest {
        val fulfillment = Fulfillment(
            invoiceId = "1", status = true,
            date = "date",time= "time", payment = "BCA",
            total = 10000000)

        val flow = flowOf(EcommerceResponse.Success("Success"))
        coEvery { setRatingTransactionUseCase.invoke("1",2,"good") } returns flow

        val job = launch {
            viewModel.eventFlow.test {
                assertEquals(
                    StatusEvent.ShowSnackbar("Success"),
                    awaitItem()
                )
                cancel()
            }
        }

        viewModel.onReviewChanged("good")
        viewModel.onRatingChanged(2)
        viewModel.setRatingTransaction(fulfillment)
        advanceUntilIdle()
        job.join()

        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertFalse(state.isError)
            assertTrue(state.isSuccess)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `setRatingTransaction failure flow updates state and emits error snackbar`() = runTest {
        val fulfillment = Fulfillment(
            invoiceId = "1", status = true,
            date = "date",time= "time", payment = "BCA",
            total = 10000000)

        val flow = flowOf(EcommerceResponse.Failure(400,"Error"))
        coEvery { setRatingTransactionUseCase.invoke("1",2,"good") } returns flow

        val job = launch {
            viewModel.eventFlow.test {
                assertEquals(
                    StatusEvent.ShowSnackbar("Error"),
                    awaitItem()
                )
                cancel()
            }
        }

        viewModel.onReviewChanged("good")
        viewModel.onRatingChanged(2)
        viewModel.setRatingTransaction(fulfillment)
        advanceUntilIdle()
        job.join()

        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertTrue(state.isError)
            assertFalse(state.isSuccess)
            cancelAndConsumeRemainingEvents()
        }
    }
}
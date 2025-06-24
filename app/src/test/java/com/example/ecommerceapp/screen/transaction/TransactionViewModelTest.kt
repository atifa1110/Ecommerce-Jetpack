package com.example.ecommerceapp.screen.transaction

import app.cash.turbine.test
import com.example.core.domain.model.TransactionModel
import com.example.core.data.network.response.EcommerceResponse
import com.example.ecommerceapp.firebase.StatusAnalytics
import com.example.core.domain.usecase.GetTransactionUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionViewModelTest {

    private lateinit var viewModel: TransactionViewModel

    private lateinit var transactionUseCase: GetTransactionUseCase
    private var statusAnalytics: StatusAnalytics = mockk(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        transactionUseCase = mockk()
        statusAnalytics = mockk(relaxed = true)
        viewModel = TransactionViewModel(transactionUseCase, statusAnalytics)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    val transaction = TransactionModel(
        invoiceId = "1",
        name = "Product",
        total = 15000000,
        payment = "BCA",
        status = true,
        date = "date",
        time = "time",
        items = listOf(),
        rating = 4,
        review = "review",
        image = "https://image.url",
    )

    @Test
    fun `getTransaction - success updates UI state with data`() = runTest {
        val flow = flowOf(EcommerceResponse.Success(listOf(transaction)))
        coEvery { transactionUseCase.invoke() } returns flow

        viewModel = TransactionViewModel(transactionUseCase, statusAnalytics)

        viewModel.getTransaction()
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertFalse(state.isError)
            assertTrue(state.isSuccess)
            assertEquals(1, state.transactions.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getTransaction - failure updates UI state with error`() = runTest {
        val flow = flowOf(EcommerceResponse.Failure(400, "error"))
        coEvery { transactionUseCase.invoke() } returns flow

        viewModel = TransactionViewModel(transactionUseCase, statusAnalytics)

        viewModel.getTransaction()
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertTrue(state.isError)
            assertFalse(state.isSuccess)
            assertEquals(0, state.transactions.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getTransaction - loading updates UI state to loading`() = runTest {
        val flow = flow {
            emit(EcommerceResponse.Loading)
            delay(100)
            emit(EcommerceResponse.Success(listOf(transaction)))
        }
        coEvery { transactionUseCase.invoke() } returns flow

        viewModel = TransactionViewModel(transactionUseCase, statusAnalytics)

        viewModel.getTransaction()
        advanceTimeBy(50) // only advance partially, to catch Loading state

        viewModel.uiState.test {
            val state1 = awaitItem() // Loading
            assertTrue(state1.isLoading)

            val state2 = awaitItem() // Success
            assertFalse(state2.isLoading)
            assertTrue(state2.isSuccess)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `reviewButtonAnalytics triggers trackReviewButtonClicked`() {
        val viewModel = TransactionViewModel(transactionUseCase, statusAnalytics)
        every { statusAnalytics.trackReviewButtonClicked() } just Runs

        viewModel.reviewButtonAnalytics()

        coVerify { statusAnalytics.trackReviewButtonClicked() }
    }

}
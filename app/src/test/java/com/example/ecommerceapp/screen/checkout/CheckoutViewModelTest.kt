package com.example.ecommerceapp.screen.checkout

import app.cash.turbine.test
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.domain.model.FulfillmentModel
import com.example.core.ui.model.Cart
import com.example.core.ui.model.Payment.PaymentItem
import com.example.core.ui.mapper.asFulfillment
import com.example.ecommerceapp.firebase.CheckoutAnalytics
import com.example.core.domain.usecase.DeleteCartUseCase
import com.example.core.domain.usecase.FulfillmentTransactionUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


@OptIn(ExperimentalCoroutinesApi::class)
class CheckoutViewModelTest {

    private lateinit var viewModel: CheckoutViewModel
    private val fulfillmentTransactionUseCase: FulfillmentTransactionUseCase = mockk()
    private val deleteCartUseCase: DeleteCartUseCase = mockk()
    private val checkoutAnalytics: CheckoutAnalytics = mockk(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = CheckoutViewModel(fulfillmentTransactionUseCase,deleteCartUseCase,checkoutAnalytics)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `calculateTotalPrice returns correct sum`() {
        val carts = listOf(
            Cart("1", "Product A", "image", "desc", 10000, 2, 0, true),
            Cart("2", "Product B", "image", "desc", 5000, 3, 0, true)
        )

        val total = viewModel.calculateTotalPrice(carts)

        assertEquals(10000 * 2 + 5000 * 3, total)
    }

    @Test
    fun `paymentButtonClick triggers analytics`() {
        coEvery { checkoutAnalytics.trackChoosePaymentButtonClicked() } just Runs
        viewModel.paymentButtonClick()
        coVerify { checkoutAnalytics.trackChoosePaymentButtonClicked() }
    }


    @Test
    fun `fulfillmentTransaction emits loading and success state`() = runTest {
        val cart = Cart("cart1", "Product","image", "RAM 16GB",10000000, 2, 10, true)
        val fulfillment = FulfillmentModel("invoice1", true, "2024-06-01", "done", "address", 2000)
        val carts = listOf(cart)
        val payment = PaymentItem("label","image",true)

        val flow = flow {
            emit(EcommerceResponse.Loading)
            delay(100)
            emit(EcommerceResponse.Success(fulfillment))
        }

        coEvery { fulfillmentTransactionUseCase.invoke(any(), any()) } returns flow
        coEvery { checkoutAnalytics.trackPayButtonClicked() } just Runs
        coEvery { checkoutAnalytics.trackFulfillmentTransaction(fulfillment.asFulfillment()) } just Runs
        coEvery { deleteCartUseCase.invoke("cart1") } just Runs

        viewModel.fulfillmentTransaction(payment, carts)
        advanceTimeBy(50)

        viewModel.uiState.test {
            val loadingState = awaitItem()
            assertTrue(loadingState.fulfillmentState.isLoading)
            assertFalse(loadingState.fulfillmentState.isSuccess)
            assertFalse(loadingState.fulfillmentState.isError)

            val successState = awaitItem()
            assertFalse(successState.fulfillmentState.isLoading)
            assertFalse(successState.fulfillmentState.isError)
            assertTrue(successState.fulfillmentState.isSuccess)
            assertEquals("invoice1",successState.fulfillmentState.fulfillment.invoiceId)

            cancelAndIgnoreRemainingEvents() // 💡 this stops collection cleanly
        }

        coVerify{ checkoutAnalytics.trackPayButtonClicked() }
        coVerify { checkoutAnalytics.trackFulfillmentTransaction(fulfillment.asFulfillment()) }
        coVerify { deleteCartUseCase.invoke("cart1") }
    }

    @Test
    fun `fulfillmentTransaction emits loading failure and snackbar event`() = runTest {
        val cart = Cart("cart1", "Product","image", "RAM 16GB",10000000, 2, 10, true)
        val carts = listOf(cart)
        val errorMessage = "Something went wrong"
        val payment = PaymentItem("label","image",true)

        val flow = flow {
            emit(EcommerceResponse.Loading)
            delay(100)
            emit(EcommerceResponse.Failure(400,errorMessage))
        }

        coEvery { fulfillmentTransactionUseCase.invoke(any(), any()) } returns flow
        coEvery { checkoutAnalytics.trackPayButtonClicked() } just Runs
        coEvery { checkoutAnalytics.trackFulfillmentTransactionFailed(errorMessage) } just Runs

        val job = launch {
            viewModel.eventFlow.test {
                val event = awaitItem()
                assertTrue(event is CheckoutEvent.ShowSnackbar)
                assertEquals(errorMessage, event.message)
                cancelAndIgnoreRemainingEvents()
            }
        }

        viewModel.fulfillmentTransaction(payment, carts)
        advanceTimeBy(10)

        viewModel.uiState.test {
            val loadingState = awaitItem()
            assertTrue(loadingState.fulfillmentState.isLoading)
            assertFalse(loadingState.fulfillmentState.isSuccess)
            assertFalse(loadingState.fulfillmentState.isError)

            val state = awaitItem()
            assertFalse(state.fulfillmentState.isLoading)
            assertTrue(state.fulfillmentState.isError)
            assertFalse(state.fulfillmentState.isSuccess)
        }

        advanceTimeBy(150)
        job.join()

        coVerify{ checkoutAnalytics.trackPayButtonClicked() }
        coVerify { checkoutAnalytics.trackFulfillmentTransactionFailed(errorMessage) }
    }
}
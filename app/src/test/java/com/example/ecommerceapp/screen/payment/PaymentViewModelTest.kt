package com.example.ecommerceapp.screen.payment

import app.cash.turbine.test
import com.example.core.domain.model.PaymentModel
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.ui.model.Payment
import com.example.ecommerceapp.firebase.CheckoutAnalytics
import com.example.core.domain.usecase.GetPaymentConfigUseCase
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
class PaymentViewModelTest {

    private val getPaymentConfigUseCase: GetPaymentConfigUseCase = mockk()
    private val checkoutAnalytics: CheckoutAnalytics = mockk(relaxed = true)

    private lateinit var viewModel: PaymentViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `paymentInfoAnalytics calls checkoutAnalytics with correct payment`() {
        // Arrange: a fake payment item
        val payment = Payment.PaymentItem(
            label = "id1",
            image = "Test Bank",
            status = true
        )
        coEvery { checkoutAnalytics.trackChoosePaymentItem(payment) } just Runs

        viewModel = PaymentViewModel(getPaymentConfigUseCase, checkoutAnalytics)
        // Act: call the analytics function
        viewModel.paymentInfoAnalytics(payment)

        // Assert: verify the method was called
        coVerify { checkoutAnalytics.trackChoosePaymentItem(payment) }
    }

    @Test
    fun `getPaymentConfig emits loading and success state`() = runTest {
        val fakeResponse = listOf(
            PaymentModel("title",listOf(PaymentModel.PaymentModelItem("label","image",true)))
        )

        val flow = flow {
            emit(EcommerceResponse.Loading)
            delay(100)
            emit(EcommerceResponse.Success(fakeResponse))
        }

        coEvery { getPaymentConfigUseCase.invoke() } returns flow

        viewModel = PaymentViewModel(getPaymentConfigUseCase, checkoutAnalytics)
        advanceTimeBy(50)

        viewModel.uiState.test {
            // Loading state
            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading)
            assertFalse(loadingState.isError)

            // Success state
            val successState = awaitItem()
            assertFalse(successState.isLoading)
            assertTrue(successState.isSuccess)
            assertEquals(1, successState.paymentItem.size)
            assertEquals("title", successState.paymentItem[0].title)

            cancel()
        }
    }

    @Test
    fun `getPaymentConfig emits loading and failure state`() = runTest {
        val flow = flow {
            emit(EcommerceResponse.Loading)
            delay(100)
            emit(EcommerceResponse.Failure(400,"Something went wrong"))
        }

        coEvery { getPaymentConfigUseCase.invoke() } returns flow

        val job = launch {
            viewModel.eventFlow.test {
                assertEquals(
                    PaymentEvent.ShowSnackbar("Something went wrong"),
                    awaitItem()
                )
                cancel()
            }
        }

        viewModel = PaymentViewModel(getPaymentConfigUseCase, checkoutAnalytics)
        advanceTimeBy(50)

        viewModel.uiState.test {
            // Loading state
            val loading = awaitItem()
            assertTrue(loading.isLoading)

            // Error state
            val failure = awaitItem()
            assertTrue(failure.isError)
            cancel()
        }

        job.join()
    }
}

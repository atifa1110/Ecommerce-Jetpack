package com.example.ecommerceapp.screen.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.ui.model.Cart
import com.example.core.ui.model.Fulfillment
import com.example.core.ui.model.Payment.PaymentItem
import com.example.core.ui.mapper.asFulfillment
import com.example.core.ui.mapper.asItemTransactionModel
import com.example.ecommerceapp.firebase.CheckoutAnalytics
import com.example.core.domain.usecase.DeleteCartUseCase
import com.example.core.domain.usecase.FulfillmentTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CheckoutUiState(
    val fulfillmentState: FulfillmentState = FulfillmentState(),
)

data class FulfillmentState(
    val isLoading : Boolean = false,
    val isError : Boolean = false,
    val isSuccess : Boolean = false,
    val fulfillment: Fulfillment = Fulfillment("",false,"","","",0)
)

sealed class CheckoutEvent {
    data class ShowSnackbar(val message: String) : CheckoutEvent()
}

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val fulfillmentTransactionUseCase: FulfillmentTransactionUseCase,
    private val deleteCartUseCase: DeleteCartUseCase,
    private val checkoutAnalytics: CheckoutAnalytics,
) : ViewModel(){

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<CheckoutEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun calculateTotalPrice(carts: List<Cart>): Int {
        return carts.sumOf { it.unitPrice * it.quantity }
    }

    private fun deleteCheckout(checkedCarts: List<Cart>) = viewModelScope.launch {
        checkedCarts.forEach { cart ->
            deleteCartUseCase.invoke(cart.productId)
        }
    }

    fun paymentButtonClick() = checkoutAnalytics.trackChoosePaymentButtonClicked()

    fun fulfillmentTransaction(payment : PaymentItem, checkedCarts: List<Cart>) = viewModelScope.launch {
        checkoutAnalytics.trackPayButtonClicked()
        val items = checkedCarts.asItemTransactionModel()
        fulfillmentTransactionUseCase(payment.label, items).collect { result ->
            when (result) {
                is EcommerceResponse.Loading -> {
                    _uiState.update {
                        it.copy(
                            fulfillmentState = it.fulfillmentState.copy(
                                isLoading = true,
                                isSuccess = false,
                                isError = false
                            )
                        )
                    }
                }

                is EcommerceResponse.Failure -> {
                    _uiState.update {
                        it.copy(
                            fulfillmentState = it.fulfillmentState.copy(
                                isLoading = false,
                                isSuccess = false,
                                isError = true
                            ),
                        )
                    }
                    _eventFlow.emit(CheckoutEvent.ShowSnackbar(result.error))
                    checkoutAnalytics.trackFulfillmentTransactionFailed(result.error)
                }

                is EcommerceResponse.Success -> {
                    deleteCheckout(checkedCarts)
                    _uiState.update {
                        it.copy(
                            fulfillmentState = it.fulfillmentState.copy(
                                isLoading = false,
                                isSuccess = true,
                                isError = false,
                                fulfillment = result.value.asFulfillment()
                            )
                        )
                    }
                    checkoutAnalytics.trackFulfillmentTransaction(result.value.asFulfillment())
                }
            }
        }
    }

}
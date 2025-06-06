package com.example.ecommerceapp.screen.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.network.response.EcommerceResponse
import com.example.ecommerceapp.data.ui.Cart
import com.example.ecommerceapp.data.ui.Fulfillment
import com.example.ecommerceapp.data.ui.Payment
import com.example.ecommerceapp.data.ui.Payment.PaymentItem
import com.example.ecommerceapp.data.ui.mapper.asFulfillment
import com.example.ecommerceapp.data.ui.mapper.asItemTransactionModel
import com.example.ecommerceapp.data.ui.mapper.asPayment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CheckoutUiState(
    val fulfillmentState: FulfillmentState = FulfillmentState(),
    val paymentConfigState: PaymentConfigState = PaymentConfigState(),
    val userMessage : String? = null,
)

data class FulfillmentState(
    val isLoading : Boolean = false,
    val isError : Boolean = false,
    val isSuccess : Boolean = false,
    val fulfillment: Fulfillment = Fulfillment("",false,"","","",0)
)

data class PaymentConfigState(
    val isLoading : Boolean = false,
    val isError : Boolean = false,
    val isSuccess : Boolean = false,
    val paymentItem : Payment.PaymentItem = Payment.PaymentItem(
        label = "Bank BCA",
        image = "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5c/Bank_Central_Asia.svg/2560px-Bank_Central_Asia.svg.png",
        status = false
    ),
)

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val fulfillmentTransactionUseCase: FulfillmentTransactionUseCase,
) : ViewModel(){

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()

    fun snackBarMessageShown(){
        _uiState.update {  it.copy(userMessage = null) }
    }

    fun calculateTotalPrice(carts: List<Cart>): Int {
        return carts.sumOf { it.unitPrice * it.quantity }
    }

    fun fulfillmentTransaction(payment : PaymentItem,checkedCarts: List<Cart>) = viewModelScope.launch {
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
                            userMessage = result.error
                        )
                    }
                }

                is EcommerceResponse.Success -> {
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
                }
            }
        }
    }

}
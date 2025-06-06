package com.example.ecommerceapp.screen.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.network.response.EcommerceResponse
import com.example.ecommerceapp.data.ui.Cart
import com.example.ecommerceapp.data.ui.Fulfillment
import com.example.ecommerceapp.data.ui.Payment
import com.example.ecommerceapp.data.ui.mapper.asFulfillment
import com.example.ecommerceapp.data.ui.mapper.asItemTransactionModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CheckoutUiState(
    val isLoading : Boolean = false,
    val isError : Boolean = false,
    val isSuccess : Boolean = false,
    val paymentItem : Payment.PaymentItem = Payment.PaymentItem(
        label = "Bank BCA",
        image = "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5c/Bank_Central_Asia.svg/2560px-Bank_Central_Asia.svg.png",
        status = false
    ),
    val userMessage : String? = null,
    val fulfillment: Fulfillment = Fulfillment("",false,"","","",0)
)

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val fulfillmentTransactionUseCase: FulfillmentTransactionUseCase
) : ViewModel(){

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()


    fun setPaymentItem(paymentItem : Payment.PaymentItem){
        _uiState.update { it.copy(paymentItem = paymentItem) }
    }

    fun snackBarMessageShown(){
        _uiState.update {  it.copy(userMessage = null) }
    }

    fun calculateTotalPrice(carts: List<Cart>): Int {
        return carts.sumOf { it.unitPrice * it.quantity }
    }

    fun fulfillmentTransaction(checkedCarts: List<Cart>) = viewModelScope.launch {
        val label = uiState.value.paymentItem.label
        val list = checkedCarts.asItemTransactionModel()
        fulfillmentTransactionUseCase.invoke(label,list).collect {result ->
            when(result){
                is EcommerceResponse.Failure -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isError = true,
                            userMessage = result.error
                        )
                    }
                }
                EcommerceResponse.Loading -> {
                    _uiState.update {
                        it.copy(
                            isLoading = true,
                            isSuccess = false
                        )
                    }
                }
                is EcommerceResponse.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true,
                            fulfillment = result.value.asFulfillment()
                        )
                    }
                }
            }
        }
    }

}
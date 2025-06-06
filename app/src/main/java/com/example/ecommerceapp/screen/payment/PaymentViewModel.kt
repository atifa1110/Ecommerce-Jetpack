package com.example.ecommerceapp.screen.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.network.response.EcommerceResponse
import com.example.ecommerceapp.data.ui.Payment
import com.example.ecommerceapp.data.ui.mapper.asPayment
import com.example.ecommerceapp.screen.checkout.GetPaymentConfigUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PaymentConfigState(
    val isLoading : Boolean = false,
    val isError : Boolean = false,
    val isSuccess : Boolean = false,
    val paymentItem : List<Payment> = emptyList(),
    val userMessage : String? = null
)


@HiltViewModel
class PaymentViewModel  @Inject constructor(
    private val getPaymentConfigUseCase: GetPaymentConfigUseCase
): ViewModel(){

    private val _uiState = MutableStateFlow(PaymentConfigState())
    val uiState: StateFlow<PaymentConfigState> = _uiState.asStateFlow()

    init {
        getPaymentConfig()
    }

    fun getPaymentConfig() = viewModelScope.launch {
        getPaymentConfigUseCase().collect { result ->
            when (result) {
                is EcommerceResponse.Loading -> {
                    _uiState.update {
                        it.copy(
                            isLoading = true,
                            isError = false,
                            isSuccess = false
                        )
                    }
                }

                is EcommerceResponse.Failure -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isError = true,
                            isSuccess = false,
                            userMessage = result.error
                        )
                    }
                }

                is EcommerceResponse.Success-> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isError = false,
                            isSuccess = true,
                            paymentItem = result.value.asPayment()
                        )
                    }
                }
            }
        }
    }
}
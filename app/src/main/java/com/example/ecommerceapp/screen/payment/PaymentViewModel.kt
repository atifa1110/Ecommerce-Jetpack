package com.example.ecommerceapp.screen.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.ui.model.Payment
import com.example.core.ui.mapper.asPayment
import com.example.ecommerceapp.firebase.CheckoutAnalytics
import com.example.core.domain.usecase.GetPaymentConfigUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PaymentConfigState(
    val isLoading : Boolean = false,
    val isError : Boolean = false,
    val isSuccess : Boolean = false,
    val paymentItem : List<Payment> = emptyList(),
)

sealed class PaymentEvent {
    data class ShowSnackbar(val message: String) : PaymentEvent()
}

@HiltViewModel
class PaymentViewModel  @Inject constructor(
    private val getPaymentConfigUseCase: GetPaymentConfigUseCase,
    private val checkoutAnalytics: CheckoutAnalytics
): ViewModel(){

    private val _uiState = MutableStateFlow(PaymentConfigState())
    val uiState: StateFlow<PaymentConfigState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<PaymentEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        getPaymentConfig()
    }

    fun paymentInfoAnalytics(payment: Payment.PaymentItem){
        checkoutAnalytics.trackChoosePaymentItem(payment)
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
                        )
                    }
                    _eventFlow.emit(PaymentEvent.ShowSnackbar(result.error))
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
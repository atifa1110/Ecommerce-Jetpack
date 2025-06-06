package com.example.ecommerceapp.screen.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.network.response.EcommerceResponse
import com.example.ecommerceapp.data.ui.Transaction
import com.example.ecommerceapp.data.ui.mapper.asTransaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TransactionUiState(
    val isLoading : Boolean = false,
    val isError : Boolean = false,
    val isSuccess : Boolean = false,
    val transactions: List<Transaction> = emptyList(),
    val userMessage : String? = null
)

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val transactionUseCase: GetTransactionUseCase
) : ViewModel(){
    private val _uiState = MutableStateFlow(TransactionUiState())
    val uiState: StateFlow<TransactionUiState> = _uiState

    fun getTransaction() = viewModelScope.launch {
        transactionUseCase.invoke().collect { result ->
            when(result){
                is EcommerceResponse.Failure -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,     // Stop loading
                            isError = true,
                            isSuccess = false,
                            userMessage = result.error)
                    }
                }
                EcommerceResponse.Loading -> {
                    _uiState.update {
                        it.copy(
                            isLoading = true,
                            isError = false,
                            isSuccess = false,
                        )
                    }
                }
                is EcommerceResponse.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,     // Stop loading
                            isError = false,
                            isSuccess = true,
                            transactions = result.value.map { it.asTransaction() })
                    }
                }
            }
        }
    }
}
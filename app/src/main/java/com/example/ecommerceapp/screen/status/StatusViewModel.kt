package com.example.ecommerceapp.screen.status

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.network.response.EcommerceResponse
import com.example.ecommerceapp.data.ui.Fulfillment
import com.example.ecommerceapp.data.ui.Review
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

import javax.inject.Inject

data class StatusUiState(
    val isLoading : Boolean = false,
    val isError : Boolean = false,
    val isSuccess : Boolean = false,
    val rating : Int = 0,
    val review: String = "",
    val userMessage : String? = null
)

@HiltViewModel
class StatusViewModel @Inject constructor(
    private val setRatingTransactionUseCase: SetRatingTransactionUseCase
) : ViewModel(){

    private val _uiState = MutableStateFlow(StatusUiState())
    val uiState: StateFlow<StatusUiState> = _uiState.asStateFlow()

    private fun isReviewInputValid(review: String, rating: Int): Boolean {
        return review.trim().isNotEmpty() && rating > 0
    }

    fun onReviewChanged(review: String) {
        _uiState.update { it.copy(review = review) }
    }

    fun onRatingChanged(rating : Int) {
        _uiState.update { it.copy(rating = rating) }
    }

    fun clearUserMessage() {
        _uiState.update { it.copy(userMessage = null) }
    }

    fun setRatingTransaction (fulfillment: Fulfillment) = viewModelScope.launch {
        val invoiceId = fulfillment.invoiceId
        val review = uiState.value.review
        val rating = uiState.value.rating

        if (!isReviewInputValid(review, rating)) {
            _uiState.update {
                it.copy(
                    isError = true,
                    userMessage = "Please enter a rating and review."
                )
            }
            return@launch
        }

        setRatingTransactionUseCase.invoke(invoiceId,rating,review).collect {result ->
            when(result){
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
                            isLoading = false,
                            isError = false,
                            isSuccess = true,
                            userMessage = result.value
                        )
                    }
                }
            }

        }
    }
}
package com.example.ecommerceapp.screen.status

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.ui.model.Fulfillment
import com.example.ecommerceapp.firebase.StatusAnalytics
import com.example.core.domain.usecase.SetRatingTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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
)

sealed class StatusEvent {
    data class ShowSnackbar(val message: String) : StatusEvent()
}

@HiltViewModel
class StatusViewModel @Inject constructor(
    private val setRatingTransactionUseCase: SetRatingTransactionUseCase,
    private val statusAnalytics: StatusAnalytics
) : ViewModel(){

    private val _uiState = MutableStateFlow(StatusUiState())
    val uiState: StateFlow<StatusUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<StatusEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private fun isReviewInputValid(review: String, rating: Int): Boolean {
        return review.trim().isNotEmpty() && rating > 0
    }

    fun onReviewChanged(review: String) {
        _uiState.update { it.copy(review = review) }
    }

    fun onRatingChanged(rating : Int) {
        _uiState.update { it.copy(rating = rating) }
    }

    fun setRatingTransaction (fulfillment: Fulfillment) = viewModelScope.launch {
        statusAnalytics.trackDoneButtonClicked()
        val invoiceId = fulfillment.invoiceId
        val review = uiState.value.review
        val rating = uiState.value.rating

        if (!isReviewInputValid(review, rating)) {
            _uiState.update {
                it.copy(
                    isError = true,
                )
            }
            _eventFlow.emit(StatusEvent.ShowSnackbar("Please enter a rating and review."))
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
                        )
                    }
                    _eventFlow.emit(StatusEvent.ShowSnackbar(result.error))
                    statusAnalytics.trackStatusTransactionFailed(result.error)
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
                        )
                    }
                    _eventFlow.emit(StatusEvent.ShowSnackbar(result.value))
                    statusAnalytics.trackStatusTransaction(result.value)
                }
            }

        }
    }
}
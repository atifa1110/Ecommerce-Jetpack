package com.example.ecommerceapp.screen.review

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.network.response.EcommerceResponse
import com.example.ecommerceapp.data.ui.Review
import com.example.ecommerceapp.data.ui.mapper.asReview
import com.example.ecommerceapp.graph.ReviewDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ReviewUiState(
    val id: String = "",
    val isLoading : Boolean = false,
    val isError : Boolean = false,
    val reviews: List<Review> = emptyList()
)
@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val getReviewProductUseCase: GetReviewProductUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(){

    private val _uiState = MutableStateFlow(getInitialUiState(savedStateHandle))
    val uiState: StateFlow<ReviewUiState> = _uiState.asStateFlow()

    private fun getInitialUiState(savedStateHandle: SavedStateHandle): ReviewUiState {
        val id = ReviewDestination.fromSavedStateHandle(savedStateHandle)
        return ReviewUiState(id = id)
    }

    fun getReviewProduct() = viewModelScope.launch {
        getReviewProductUseCase.invoke(uiState.value.id).collect { result->
            when(result){
                is EcommerceResponse.Failure -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isError = true,
                        )
                    }
                }
                EcommerceResponse.Loading -> {
                    _uiState.update {
                        it.copy(
                            isLoading = true,
                            isError = false
                        )
                    }
                }
                is EcommerceResponse.Success-> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isError = false,
                            reviews = result.value.map { it.asReview() }
                        )
                    }
                }
            }
        }
    }
}
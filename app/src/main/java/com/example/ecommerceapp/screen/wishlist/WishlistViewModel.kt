package com.example.ecommerceapp.screen.wishlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.ui.model.Wishlist
import com.example.core.ui.mapper.asCartModel
import com.example.core.ui.mapper.asWishlist
import com.example.ecommerceapp.firebase.WishlistAnalytics
import com.example.core.domain.usecase.AddToCartUseCase
import com.example.core.domain.usecase.GetWishlistUseCase
import com.example.core.domain.usecase.RemoveFromWishlistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WishlistUiState(
    val isLoading : Boolean = false,
    val isClickedGrid : Boolean = false,
    val isError : Boolean = false,
    val wishlists : List<Wishlist> = emptyList()
)

sealed class WishlistEvent {
    data class ShowSnackbar(val message: String) : WishlistEvent()
}

@HiltViewModel
class WishlistViewModel @Inject constructor(
    private val getWishlistUseCase: GetWishlistUseCase,
    private val removeFromWishlistUseCase: RemoveFromWishlistUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val wishlistAnalytics: WishlistAnalytics
) : ViewModel(){

    private val _uiState = MutableStateFlow(WishlistUiState())
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<WishlistEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun setClickedGrid(){
        _uiState.update { it.copy(isClickedGrid = !uiState.value.isClickedGrid) }
        wishlistAnalytics.trackGridView(uiState.value.isClickedGrid)
    }

    fun deleteWishlist(id: String) = viewModelScope.launch {
        removeFromWishlistUseCase.invoke(id)
        _uiState.update { currentState ->
            currentState.copy(
                wishlists = currentState.wishlists.filterNot { it.productId == id },
            )
        }
        wishlistAnalytics.trackDeleteWishlistButtonClicked(id)
        _eventFlow.emit(WishlistEvent.ShowSnackbar("Success Remove from WishList"))
    }

    fun addToCart(id: String) = viewModelScope.launch{
        wishlistAnalytics.trackAddCartButtonClicked()
        val wishlistItem = uiState.value.wishlists.find { it.productId == id}
        if(wishlistItem!=null) {
            val cartModel = wishlistItem.asCartModel(wishlistItem.unitPrice, wishlistItem.variantName)
            addToCartUseCase.invoke(cartModel)
            wishlistAnalytics.trackAddToCart(cartModel,1)
            _eventFlow.emit(WishlistEvent.ShowSnackbar("Success Add to Cart"))
        }
    }

    fun getWishlist() = viewModelScope.launch {
        getWishlistUseCase.invoke().collect { result ->
            when(result){
                is EcommerceResponse.Failure -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isError = true,
                        )
                    }
                    _eventFlow.emit(WishlistEvent.ShowSnackbar(result.error))
                    wishlistAnalytics.trackWishlistFailed(result.error)
                }
                EcommerceResponse.Loading -> {
                    _uiState.update {
                        it.copy(
                            isLoading = true,
                            isError = false,
                        )
                    }
                }
                is EcommerceResponse.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isError = false,
                            wishlists = result.value.map {  it.asWishlist() }
                        )
                    }
                    wishlistAnalytics.trackViewWishlist(result.value.map {  it.asWishlist() })
                }
            }
        }
    }
}
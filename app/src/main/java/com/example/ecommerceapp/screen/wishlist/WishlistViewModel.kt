package com.example.ecommerceapp.screen.wishlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.network.response.EcommerceResponse
import com.example.ecommerceapp.data.ui.ProductVariant
import com.example.ecommerceapp.data.ui.Wishlist
import com.example.ecommerceapp.data.ui.mapper.asCartModel
import com.example.ecommerceapp.data.ui.mapper.asWishlist
import com.example.ecommerceapp.screen.detail.AddToCartUseCase
import com.example.ecommerceapp.screen.detail.RemoveFromWishlistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WishlistUiState(
    val isLoading : Boolean = false,
    val isClickedGrid : Boolean = false,
    val isError : Boolean = false,
    val userMessage : String? = null,
    val wishlists : List<Wishlist> = emptyList()
)

@HiltViewModel
class WishlistViewModel @Inject constructor(
    private val getWishlistUseCase: GetWishlistUseCase,
    private val removeFromWishlistUseCase: RemoveFromWishlistUseCase,
    private val addToCartUseCase: AddToCartUseCase
) : ViewModel(){

    private val _uiState = MutableStateFlow(WishlistUiState())
    val uiState = _uiState.asStateFlow()

    fun setClickedGrid(){
        _uiState.update { it.copy(isClickedGrid = !uiState.value.isClickedGrid) }
    }
    fun snackBarMessageShown() {
        _uiState.update { it.copy(userMessage = null) }
    }

    fun deleteWishlist(id: String) = viewModelScope.launch {
        removeFromWishlistUseCase.invoke(id)
        _uiState.update { currentState ->
            currentState.copy(
                wishlists = currentState.wishlists.filterNot { it.productId == id },
                userMessage = "Success Remove from WishList"
            )
        }
    }

    fun addToCart(id: String) = viewModelScope.launch{
        val wishlistItem = uiState.value.wishlists.find { it.productId == id}
        if(wishlistItem!=null) {
            val cartModel = wishlistItem.asCartModel(wishlistItem.unitPrice, wishlistItem.variantName)
            addToCartUseCase.invoke(cartModel)
            _uiState.update { it.copy(userMessage = "Success Add To Cart") }
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
                            userMessage = result.error
                        )
                    }
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
                }
            }
        }
    }
}
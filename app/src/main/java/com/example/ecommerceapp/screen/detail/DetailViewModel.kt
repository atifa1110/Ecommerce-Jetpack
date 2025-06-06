package com.example.ecommerceapp.screen.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.network.response.EcommerceResponse
import com.example.ecommerceapp.data.ui.ProductDetail
import com.example.ecommerceapp.data.ui.ProductVariant
import com.example.ecommerceapp.data.ui.mapper.asCartModel
import com.example.ecommerceapp.data.ui.mapper.asProductDetail
import com.example.ecommerceapp.data.ui.mapper.asWishlistModel
import com.example.ecommerceapp.graph.DetailsDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailUiState(
    val id: String = "",
    val isLoading : Boolean = false,
    val isError : Boolean = false,
    val productDetail : ProductDetail = ProductDetail(),
    val selectedVariant: ProductVariant? = null,
    val totalPrice: Int = 0,
    val userMessage : String? = null
)

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getDetailProductUseCase: GetDetailProductUseCase,
    private val addToWishlistUseCase: AddToWishlistUseCase,
    private val removeFromWishlistUseCase: RemoveFromWishlistUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(){

    private val _uiState = MutableStateFlow(getInitialUiState(savedStateHandle))
    val uiState = _uiState.asStateFlow()

    private fun getInitialUiState(savedStateHandle: SavedStateHandle): DetailUiState {
        val id = DetailsDestination.fromSavedStateHandle(savedStateHandle)
        return DetailUiState(id = id)
    }

    fun onAddToCart() = viewModelScope.launch {
        val productDetail = uiState.value.productDetail
        val selectedVariant = uiState.value.selectedVariant?: ProductVariant("",0)
        val cartModel = productDetail.asCartModel(selectedVariant)
        addToCartUseCase.invoke(cartModel)
        setUserMessage("Success Add to Cart")
    }

    fun onWishlistDetail(){
        val updatedProduct = uiState.value.productDetail.copy(
            isWishlist = !uiState.value.productDetail.isWishlist
        )
        _uiState.update {
            it.copy(productDetail = updatedProduct)
        }
        viewModelScope.launch {
            uiState.value.productDetail.let { product ->
                if (product.isWishlist) {
                    val selectedVariant = uiState.value.selectedVariant?.variantName?:""
                    val wishlistModel = product.asWishlistModel(uiState.value.totalPrice,selectedVariant)
                    addToWishlistUseCase.invoke(wishlistModel)
                    setUserMessage("Success Add to WishList")
                } else {
                    removeFromWishlistUseCase.invoke(product.productId?:"")
                    setUserMessage("Success Remove from WishList")
                }
            }
        }
    }

    fun snackBarMessageShown(){
        _uiState.update {
            it.copy(userMessage = null)
        }
    }

    private fun setUserMessage(userMessage: String){
        _uiState.update {
            it.copy(userMessage = userMessage)
        }
    }

    fun onVariantSelected(variant: ProductVariant) {
        val currentState = _uiState.value
        val basePrice = currentState.productDetail.productPrice ?: 0

        val isSame = currentState.selectedVariant == variant
        val newSelected = if (isSame) null else variant // Optional: allow deselection
        val variantPrice = newSelected?.variantPrice ?: 0
        val total = basePrice + variantPrice

        _uiState.update {
            it.copy(
                selectedVariant = newSelected,
                totalPrice = total
            )
        }
    }

    fun loadDetailProduct() = viewModelScope.launch {
        getDetailProductUseCase.invoke(uiState.value.id).collect { result->
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
                            isLoading = true
                        )
                    }
                }
                is EcommerceResponse.Success-> {
                    val productDetail = result.value.asProductDetail()

                    val firstVariant = productDetail.productVariant?.firstOrNull()
                    val basePrice = productDetail.productPrice ?: 0
                    val variantPrice = firstVariant?.variantPrice ?: 0
                    val total = basePrice + variantPrice

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            productDetail = productDetail,
                            selectedVariant = firstVariant,
                            totalPrice = total
                        )
                    }
                }
            }
        }
    }
}
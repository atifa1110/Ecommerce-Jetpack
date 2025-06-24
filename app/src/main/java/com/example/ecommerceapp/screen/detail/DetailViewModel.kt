package com.example.ecommerceapp.screen.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.ui.model.ProductDetail
import com.example.core.ui.model.ProductVariant
import com.example.core.ui.mapper.asCartModel
import com.example.core.ui.mapper.asProductDetail
import com.example.core.ui.mapper.asWishlistModel
import com.example.ecommerceapp.firebase.ProductAnalyticsManager
import com.example.ecommerceapp.graph.DetailsDestination
import com.example.core.domain.usecase.AddToCartUseCase
import com.example.core.domain.usecase.AddToWishlistUseCase
import com.example.core.domain.usecase.GetDetailProductUseCase
import com.example.core.domain.usecase.RemoveFromWishlistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
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
    val totalPrice: Int = 0
)

sealed class DetailEvent{
    data class ShowSnackbar(val message: String) : DetailEvent()
}

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getDetailProductUseCase: GetDetailProductUseCase,
    private val addToWishlistUseCase: AddToWishlistUseCase,
    private val removeFromWishlistUseCase: RemoveFromWishlistUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val productAnalyticsManager: ProductAnalyticsManager,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(){

    private val _uiState = MutableStateFlow(getInitialUiState(savedStateHandle))
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<DetailEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private fun getInitialUiState(savedStateHandle: SavedStateHandle): DetailUiState {
        val id = DetailsDestination.fromSavedStateHandle(savedStateHandle)
        return DetailUiState(id = id)
    }

    fun onAddToCart() = viewModelScope.launch {
        productAnalyticsManager.trackDetailAddToCartButtonClicked()
        val productDetail = uiState.value.productDetail
        val selectedVariant = uiState.value.selectedVariant?: ProductVariant("",0)
        val cartModel = productDetail.asCartModel(selectedVariant)
        addToCartUseCase.invoke(cartModel)
        _eventFlow.emit(DetailEvent.ShowSnackbar("Success Add to Cart"))
    }

    fun checkoutAnalytics(){
        productAnalyticsManager.trackDetailAddToCheckoutButtonClicked()
    }

    fun onWishlistDetail() {
        val newWishlistStatus = !uiState.value.productDetail.isWishlist
        val updatedProduct = uiState.value.productDetail.copy(isWishlist = newWishlistStatus)

        _uiState.update {
            it.copy(productDetail = updatedProduct)
        }

        viewModelScope.launch {
            productAnalyticsManager.trackDetailWishlistButtonClicked(uiState.value.id, newWishlistStatus)

            if (newWishlistStatus) {
                val selectedVariant = uiState.value.selectedVariant?.variantName.orEmpty()
                val wishlistModel = updatedProduct.asWishlistModel(uiState.value.totalPrice, selectedVariant)
                addToWishlistUseCase.invoke(wishlistModel)
                _eventFlow.emit(DetailEvent.ShowSnackbar("Success Add to WishList"))
            } else {
                removeFromWishlistUseCase.invoke(updatedProduct.productId.orEmpty())
                _eventFlow.emit(DetailEvent.ShowSnackbar("Success Remove from WishList"))
            }
        }
    }

    fun onVariantSelected(variant: ProductVariant) {
        val currentState = _uiState.value
        val basePrice = currentState.productDetail.productPrice ?: 0

        val isSame = currentState.selectedVariant == variant
        val newSelected = if (isSame) null else variant
        val variantPrice = newSelected?.variantPrice ?: 0
        val total = basePrice + variantPrice

        _uiState.update {
            it.copy(
                selectedVariant = newSelected,
                totalPrice = total
            )
        }
        productAnalyticsManager.trackSelectedVariantDetail(uiState.value.id,variant)
    }

    fun loadDetailProduct() = viewModelScope.launch {
        getDetailProductUseCase.invoke(uiState.value.id).collect { result->
            when(result){
                is EcommerceResponse.Failure -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isError = true,
                        )
                    }
                    _eventFlow.emit(DetailEvent.ShowSnackbar(result.error))
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

                    productAnalyticsManager.trackViewItemDetail(productDetail)
                }
            }
        }
    }
}
package com.example.ecommerceapp.screen.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.network.response.EcommerceResponse
import com.example.ecommerceapp.data.ui.Cart
import com.example.ecommerceapp.data.ui.mapper.asCart
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CartUiState(
    val isLoading : Boolean = false,
    val isError : Boolean = false,
    val carts : List<Cart> = emptyList(),
    val userMessage : String? = null
)

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartUseCase: GetCartUseCase,
    private val updateCartQuantityUseCase: UpdateCartQuantityUseCase,
    private val deleteCartUseCase: DeleteCartUseCase
) : ViewModel(){

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    val isAllChecked: StateFlow<Boolean> = _uiState
        .map { items -> items.carts.isNotEmpty() && items.carts.all { it.isCheck } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val isAnyItemChecked: StateFlow<Boolean> = _uiState
        .map { list -> list.carts.any { it.isCheck } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val checkedCarts: StateFlow<List<Cart>> = _uiState
        .map { it.carts.filter { it.isCheck } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalCheckedPrice: StateFlow<Int> = uiState
        .map { state -> state.carts.filter { it.isCheck }
                .sumOf { it.unitPrice * it.quantity }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun loadCartItems() = viewModelScope.launch {
         getCartUseCase.invoke().collect { result ->
             when(result){
                 is EcommerceResponse.Failure -> {
                     _uiState.update {
                         it.copy(isLoading = false,isError = true)
                     }
                 }
                 EcommerceResponse.Loading -> {
                     _uiState.update {
                         it.copy(isLoading = true,isError = false)
                     }
                 }
                 is EcommerceResponse.Success -> {
                     _uiState.update {
                         it.copy(isLoading = false, carts = result.value.map { it.asCart() })
                     }
                 }
             }
        }
    }

    fun toggleItemChecked(productId: String) {
        _uiState.update { state ->
            val updated = state.carts.map {
                if (it.productId == productId) it.copy(isCheck = !it.isCheck) else it
            }
            state.copy(carts = updated)
        }
    }

    fun selectAll() {
        _uiState.update { state ->
            state.copy(carts = state.carts.map { it.copy(isCheck = true) })
        }
    }

    fun clearSelection() {
        _uiState.update { state ->
            state.copy(carts = state.carts.map { it.copy(isCheck = false) })
        }
    }

    fun increaseQuantity(productId: String) {
        _uiState.update { state ->
            val updatedCarts = state.carts.map {
                if (it.productId == productId && it.quantity < it.stock) {
                    val newQty = it.quantity + 1
                    viewModelScope.launch {
                        updateCartQuantityUseCase(productId, newQty)
                    }
                    it.copy(quantity = newQty)
                } else it
            }
            state.copy(carts = updatedCarts)
        }
    }

    fun decreaseQuantity(productId: String) {
        _uiState.update { state ->
            val updatedCarts = state.carts.map {
                if (it.productId == productId && it.quantity > 1) {
                    val newQty = it.quantity - 1
                    viewModelScope.launch {
                        updateCartQuantityUseCase(productId, newQty)
                    }
                    it.copy(quantity = newQty)
                } else it
            }
            state.copy(carts = updatedCarts)
        }
    }

    fun deleteCartItem(productId: String) {
        viewModelScope.launch {
            deleteCartUseCase(productId)
            // Update local state after deletion
            _uiState.update { state ->
                state.copy(
                    carts = state.carts.filterNot { it.productId == productId }
                )
            }
        }
    }


    fun deleteCheckedItems() {
        val checkedItems = _uiState.value.carts.filter { it.isCheck }
        viewModelScope.launch {
            checkedItems.forEach { cart ->
                deleteCartUseCase(cart.productId)
            }
            // Remove from local state
            _uiState.update { state ->
                state.copy(carts = state.carts.filterNot { it.isCheck })
            }
        }
    }


}
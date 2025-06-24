package com.example.ecommerceapp.screen.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.ui.model.Cart
import com.example.core.ui.model.Fulfillment
import com.example.core.ui.model.ProductDetail
import com.example.core.ui.model.ProductVariant
import com.example.core.ui.model.Transaction
import com.example.core.ui.mapper.asCart
import com.example.core.ui.mapper.asFulfillment
import com.example.ecommerceapp.firebase.CartAnalytics
import com.example.core.domain.usecase.UpdateCartQuantityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SharedUiState(
    val checkedCarts : List<Cart> = emptyList(),
    val fulfillment: Fulfillment = Fulfillment("",false,"","","",0)
)

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val updateCartQuantityUseCase: UpdateCartQuantityUseCase,
    private val cartAnalytics: CartAnalytics
): ViewModel() {

    private val _uiState = MutableStateFlow(SharedUiState())
    val uiState: StateFlow<SharedUiState> = _uiState.asStateFlow()

    fun setCheckedCartItems(cart: List<Cart>) {
        _uiState.update {it.copy(checkedCarts = cart)}
        cartAnalytics.trackCheckedCartItems(cart)
    }

    fun setDetailCartItems(selectVariant: ProductVariant,detail : ProductDetail) {
        val cart = detail.asCart(selectVariant)
        _uiState.update {it.copy(checkedCarts = listOf(cart))}
        cartAnalytics.trackDetailCartItemSelected(detail,selectVariant)
    }

    fun clearCheckedCarts() {
        _uiState.update { it.copy(checkedCarts = emptyList()) }
    }

    fun setTransaction(transaction: Transaction){
        _uiState.update { it.copy(fulfillment = transaction.asFulfillment()) }
    }

    fun clearFulfillment() {
        _uiState.update { state ->
            state.copy(fulfillment = Fulfillment("",false,"","","",0))
        }
    }

    fun setFulfillment(fulfillment: Fulfillment) {
        _uiState.update { state ->
            state.copy(fulfillment = fulfillment)
        }
    }

    fun updateQuantity(productId: String, isIncrement: Boolean) {
        val cartItem = _uiState.value.checkedCarts.find { it.productId == productId } ?: return

        val newQty = if (isIncrement) cartItem.quantity + 1 else cartItem.quantity - 1

        // Only update if new quantity is within valid range
        val isValid = (isIncrement && newQty <= cartItem.stock) || (!isIncrement && newQty >= 1)
        if (!isValid) return

        // Update local state
        _uiState.update { state ->
            val updatedCarts = state.checkedCarts.map {
                if (it.productId == productId) it.copy(quantity = newQty) else it
            }
            state.copy(checkedCarts = updatedCarts)
        }

        // Sync with server and track analytics
        viewModelScope.launch {
            updateCartQuantityUseCase(productId, newQty)
        }
    }
}
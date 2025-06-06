package com.example.ecommerceapp.screen.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.ui.Cart
import com.example.ecommerceapp.data.ui.Fulfillment
import com.example.ecommerceapp.data.ui.ProductDetail
import com.example.ecommerceapp.data.ui.ProductVariant
import com.example.ecommerceapp.data.ui.Transaction
import com.example.ecommerceapp.data.ui.mapper.asCart
import com.example.ecommerceapp.data.ui.mapper.asFulfillment
import com.example.ecommerceapp.graph.MainLevelDestination
import com.example.ecommerceapp.screen.cart.UpdateCartQuantityUseCase
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
): ViewModel() {

    private val _bottomNavStart = MutableStateFlow(MainLevelDestination.Home)
    val bottomNavStart: StateFlow<MainLevelDestination> = _bottomNavStart

    fun setBottomNavStart(destination: MainLevelDestination) {
        _bottomNavStart.value = destination
    }

    private val _uiState = MutableStateFlow(SharedUiState())
    val uiState: StateFlow<SharedUiState> = _uiState.asStateFlow()

    fun setCheckedCartItems(cart: List<Cart>) {
        _uiState.update {it.copy(checkedCarts = cart)}
    }

    fun setDetailCartItems(selectVariant: ProductVariant,detail : ProductDetail) {
        val cart = detail.asCart(selectVariant)
        _uiState.update {it.copy(checkedCarts = listOf(cart))}
    }

    fun clearCheckedCarts() {
        _uiState.update { it.copy(checkedCarts = emptyList()) }
    }

    fun setTransaction(transaction: Transaction){
        _uiState.update { it.copy(fulfillment = transaction.asFulfillment()) }
    }

    fun setFulfillment(fulfillment: Fulfillment) {
        _uiState.update { state ->
            state.copy(fulfillment = fulfillment)
        }
    }


    fun increaseQuantity(productId: String) {
        _uiState.update { state ->
            val updatedCarts = state.checkedCarts.map {
                if (it.productId == productId && it.quantity < it.stock) {
                    val newQty = it.quantity + 1
                    viewModelScope.launch {
                        updateCartQuantityUseCase(productId, newQty)
                    }
                    it.copy(quantity = newQty)
                } else it
            }
            state.copy(checkedCarts = updatedCarts)
        }
    }

    fun decreaseQuantity(productId: String) {
        _uiState.update { state ->
            val updatedCarts = state.checkedCarts.map {
                if (it.productId == productId && it.quantity > 1) {
                    val newQty = it.quantity - 1
                    viewModelScope.launch {
                        updateCartQuantityUseCase(productId, newQty)
                    }
                    it.copy(quantity = newQty)
                } else it
            }
            state.copy(checkedCarts = updatedCarts)
        }
    }

}
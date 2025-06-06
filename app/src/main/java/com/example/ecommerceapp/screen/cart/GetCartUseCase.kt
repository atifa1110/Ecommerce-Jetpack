package com.example.ecommerceapp.screen.cart

import com.example.ecommerceapp.data.domain.CartModel
import com.example.ecommerceapp.data.network.response.EcommerceResponse
import com.example.ecommerceapp.repository.cart.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    operator fun invoke() : Flow<EcommerceResponse<List<CartModel>>>{
        return cartRepository.getAllCart()
    }
}
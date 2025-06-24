package com.example.core.domain.usecase

import com.example.core.data.network.response.EcommerceResponse
import com.example.core.domain.model.CartModel
import com.example.core.domain.repository.cart.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    operator fun invoke() : Flow<EcommerceResponse<List<CartModel>>>{
        return cartRepository.getAllCart()
    }
}
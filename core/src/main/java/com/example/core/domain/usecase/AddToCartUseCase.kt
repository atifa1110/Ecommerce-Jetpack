package com.example.core.domain.usecase

import com.example.core.domain.model.CartModel
import com.example.core.domain.repository.cart.CartRepository
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
){
    suspend operator fun invoke(cartModel: CartModel) {
        return cartRepository.addToCart(cartModel)
    }
}
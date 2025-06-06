package com.example.ecommerceapp.screen.detail

import com.example.ecommerceapp.data.domain.CartModel
import com.example.ecommerceapp.repository.cart.CartRepository
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
){
    suspend operator fun invoke(cartModel: CartModel) {
        return cartRepository.addToCart(cartModel)
    }
}
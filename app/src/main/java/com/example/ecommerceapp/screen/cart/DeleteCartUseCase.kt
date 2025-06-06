package com.example.ecommerceapp.screen.cart

import com.example.ecommerceapp.repository.cart.CartRepository
import javax.inject.Inject

class DeleteCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(productId: String) {
        cartRepository.removeFromCartById(productId)
    }
}
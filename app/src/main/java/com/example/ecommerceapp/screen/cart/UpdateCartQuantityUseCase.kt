package com.example.ecommerceapp.screen.cart

import com.example.ecommerceapp.repository.cart.CartRepository
import javax.inject.Inject

class UpdateCartQuantityUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(productId: String, quantity: Int) {
        cartRepository.updateCartQuantity(productId, quantity)
    }
}

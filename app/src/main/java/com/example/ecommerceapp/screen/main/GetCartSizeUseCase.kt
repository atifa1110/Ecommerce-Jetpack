package com.example.ecommerceapp.screen.main

import com.example.ecommerceapp.repository.cart.CartRepository
import javax.inject.Inject

class GetCartSizeUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(): Int {
        return cartRepository.getCartSize()
    }
}

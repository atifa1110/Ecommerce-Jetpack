package com.example.core.domain.usecase

import com.example.core.domain.repository.cart.CartRepository
import javax.inject.Inject

class GetCartSizeUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(): Int {
        return cartRepository.getCartSize()
    }
}

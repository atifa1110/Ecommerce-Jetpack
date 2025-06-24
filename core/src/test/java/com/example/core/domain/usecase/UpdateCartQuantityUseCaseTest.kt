package com.example.core.domain.usecase

import com.example.core.domain.repository.cart.CartRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpdateCartQuantityUseCaseTest {

    private lateinit var cartRepository: CartRepository
    private lateinit var useCase: UpdateCartQuantityUseCase

    @Before
    fun setUp() {
        cartRepository = mockk(relaxed = true)
        useCase = UpdateCartQuantityUseCase(cartRepository)
    }

    @Test
    fun `invoke should call updateCartQuantity with correct params`() = runTest {
        // Given
        val productId = "product-123"
        val quantity = 3

        // When
        useCase(productId, quantity)

        // Then
        coVerify { cartRepository.updateCartQuantity(productId, quantity) }
    }
}

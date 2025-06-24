package com.example.core.domain.usecase

import com.example.core.domain.repository.cart.CartRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DeleteCartUseCaseTest {

    private lateinit var cartRepository: CartRepository
    private lateinit var deleteCartUseCase: DeleteCartUseCase

    @Before
    fun setUp() {
        cartRepository = mockk(relaxed = true)
        deleteCartUseCase = DeleteCartUseCase(cartRepository)
    }

    @Test
    fun `invoke should call removeFromCartById with correct productId`() = runTest {
        // Given
        val productId = "product123"
        coEvery { cartRepository.removeFromCartById(productId) } returns Unit

        // When
        deleteCartUseCase(productId)

        // Then
        coVerify(exactly = 1) { cartRepository.removeFromCartById(productId) }
    }
}

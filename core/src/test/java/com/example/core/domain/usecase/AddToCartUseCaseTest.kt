package com.example.core.domain.usecase

import com.example.core.domain.model.CartModel
import com.example.core.domain.repository.cart.CartRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddToCartUseCaseTest {

    private lateinit var repository: CartRepository
    private lateinit var useCase: AddToCartUseCase

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        useCase = AddToCartUseCase(repository)
    }

    @Test
    fun `invoke should call addToCart with correct cartModel`() = runTest {
        // Given
        val cartModel = CartModel(
            productId = "P123",
            productName = "name",
            image = "image",
            variantName = "variant",
            unitPrice = 1000,
            quantity = 2,
            stock = 4
        )

        coEvery { repository.addToCart(cartModel) } returns Unit

        // When
        useCase(cartModel)

        // Then
        coVerify(exactly = 1) { repository.addToCart(cartModel) }
    }
}

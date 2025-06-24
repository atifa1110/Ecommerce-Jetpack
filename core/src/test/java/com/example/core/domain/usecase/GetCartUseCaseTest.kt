package com.example.core.domain.usecase

import app.cash.turbine.test
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.domain.model.CartModel
import com.example.core.domain.repository.cart.CartRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class GetCartUseCaseTest {

    private lateinit var repository: CartRepository
    private lateinit var useCase: GetCartUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetCartUseCase(repository)
    }

    @Test
    fun `invoke should emit list of cart items wrapped in success response`() = runTest {
        // Given
        val cartItems = listOf(CartModel("id1", "Product 1", "image","variant",2,2,2))
        val expected = EcommerceResponse.Success(cartItems)

        coEvery { repository.getAllCart() } returns flowOf(expected)

        // When & Then
        useCase().test {
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }
}

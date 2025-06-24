package com.example.core.domain.usecase

import com.example.core.domain.repository.cart.CartRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class GetCartSizeUseCaseTest {

    private lateinit var cartRepository: CartRepository
    private lateinit var useCase: GetCartSizeUseCase

    @Before
    fun setUp() {
        cartRepository = mockk()
        useCase = GetCartSizeUseCase(cartRepository)
    }

    @Test
    fun `invoke should return correct cart size`() = runTest {
        // Given
        val expectedSize = 5
        coEvery { cartRepository.getCartSize() } returns expectedSize

        // When
        val result = useCase()

        // Then
        assertEquals(expectedSize, result)
        coVerify(exactly = 1) { cartRepository.getCartSize() }
    }
}

package com.example.core.domain.usecase

import com.example.core.domain.repository.wishlist.WishlistRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class GetWishlistSizeUseCaseTest {

    private lateinit var repository: WishlistRepository
    private lateinit var useCase: GetWishlistSizeUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetWishlistSizeUseCase(repository)
    }

    @Test
    fun `invoke should return wishlist size`() = runTest {
        // Given
        val expectedSize = 5
        coEvery { repository.getWishlistSize() } returns expectedSize

        // When
        val result = useCase()

        // Then
        assertEquals(expectedSize, result)
    }
}

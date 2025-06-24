package com.example.core.domain.usecase

import com.example.core.data.network.response.EcommerceResponse
import com.example.core.domain.repository.payment.PaymentRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.first

class SetRatingTransactionUseCaseTest {

    private lateinit var paymentRepository: PaymentRepository
    private lateinit var useCase: SetRatingTransactionUseCase

    @Before
    fun setUp() {
        paymentRepository = mockk()
        useCase = SetRatingTransactionUseCase(paymentRepository)
    }

    @Test
    fun `invoke should return success response from repository`() = runTest {
        // Given
        val invoiceId = "INV123"
        val rating = 5
        val review = "Excellent service"
        val expected = EcommerceResponse.Success("Thank you")

        coEvery {
            paymentRepository.setRatingTransaction(invoiceId, rating, review)
        } returns flowOf(expected)

        // When
        val result = useCase(invoiceId, rating, review).first()

        // Then
        assertEquals(expected, result)
    }
}

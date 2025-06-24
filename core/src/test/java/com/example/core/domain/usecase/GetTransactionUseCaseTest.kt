package com.example.core.domain.usecase

import app.cash.turbine.test
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.domain.model.ItemTransactionModel
import com.example.core.domain.model.TransactionModel
import com.example.core.domain.repository.payment.PaymentRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class GetTransactionUseCaseTest {

    private lateinit var repository: PaymentRepository
    private lateinit var useCase: GetTransactionUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetTransactionUseCase(repository)
    }

    @Test
    fun `invoke should return transaction list from repository`() = runTest {
        // Given
        val transactions = listOf(
            TransactionModel(
                invoiceId = "INV123",
                status = true,
                date = "2025-06-16",
                time = "14:00",
                payment = "OVO",
                total = 150000,
                items = listOf(
                    ItemTransactionModel("P001", "Size M", 2)
                ),
                rating = 5,
                review = "Sangat puas!",
                image = "image",
                name = "Adit"
            ),
        )
        val expectedResponse = EcommerceResponse.Success(transactions)

        coEvery { repository.getTransaction() } returns flowOf(expectedResponse)

        // When & Then
        useCase().test {
            val result = awaitItem()
            assertEquals(expectedResponse, result)
            awaitComplete()
        }
    }
}

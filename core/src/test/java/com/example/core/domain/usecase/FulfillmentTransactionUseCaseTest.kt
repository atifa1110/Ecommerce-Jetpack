package com.example.core.domain.usecase

import app.cash.turbine.test
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.domain.model.FulfillmentModel
import com.example.core.domain.model.ItemTransactionModel
import com.example.core.domain.repository.payment.PaymentRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class FulfillmentTransactionUseCaseTest {

    private lateinit var repository: PaymentRepository
    private lateinit var useCase: FulfillmentTransactionUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = FulfillmentTransactionUseCase(repository)
    }

    @Test
    fun `invoke should return flow of EcommerceResponse`() = runTest {
        // Given
        val payment = "credit_card"
        val items = listOf(ItemTransactionModel("id1","variant",2))
        val expected = EcommerceResponse.Success(
            FulfillmentModel("transaction123",true,"date","time","payment",11000))

        coEvery {
            repository.fulfillmentTransaction(payment, items)
        } returns flowOf(expected)

        // When & Then
        useCase(payment, items).test {
            val result = awaitItem()
            assertEquals(expected, result)
            awaitComplete()
        }
    }
}

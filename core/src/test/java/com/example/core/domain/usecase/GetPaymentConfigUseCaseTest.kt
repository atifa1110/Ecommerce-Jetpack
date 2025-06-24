package com.example.core.domain.usecase

import app.cash.turbine.test
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.domain.model.PaymentModel
import com.example.core.domain.repository.payment.PaymentRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class GetPaymentConfigUseCaseTest {

    private lateinit var repository: PaymentRepository
    private lateinit var useCase: GetPaymentConfigUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetPaymentConfigUseCase(repository)
    }

    @Test
    fun `invoke should return payment config response`() = runTest {
        // Given
        val fakePayments = listOf(
            PaymentModel(
                title = "E-Wallet",
                item = listOf(
                    PaymentModel.PaymentModelItem("OVO", "ovo.png", true),
                    PaymentModel.PaymentModelItem("GoPay", "gopay.png", true)
                )
            )
        )
        val expectedResponse = EcommerceResponse.Success(fakePayments)
        coEvery { repository.getPaymentConfig() } returns flowOf(expectedResponse)

        // When & Then
        useCase().test {
            assertEquals(expectedResponse, awaitItem())
            awaitComplete()
        }
    }
}
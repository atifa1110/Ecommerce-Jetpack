package com.example.ecommerceapp.screen.checkout

import com.example.ecommerceapp.data.domain.ItemTransactionModel
import com.example.ecommerceapp.data.domain.PaymentModel
import com.example.ecommerceapp.data.network.response.EcommerceResponse
import com.example.ecommerceapp.repository.payment.PaymentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPaymentConfigUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) {
    operator fun invoke() : Flow<EcommerceResponse<List<PaymentModel>>>{
        return paymentRepository.getPaymentConfig()
    }
}
package com.example.core.domain.usecase

import com.example.core.domain.model.PaymentModel
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.domain.repository.payment.PaymentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPaymentConfigUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) {
    operator fun invoke() : Flow<EcommerceResponse<List<PaymentModel>>>{
        return paymentRepository.getPaymentConfig()
    }
}
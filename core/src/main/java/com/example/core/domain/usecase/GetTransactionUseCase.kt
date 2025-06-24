package com.example.core.domain.usecase

import com.example.core.domain.model.TransactionModel
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.domain.repository.payment.PaymentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTransactionUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) {
    operator fun invoke() : Flow<EcommerceResponse<List<TransactionModel>>> {
        return paymentRepository.getTransaction()
    }
}
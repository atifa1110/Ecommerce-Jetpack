package com.example.ecommerceapp.screen.transaction

import com.example.ecommerceapp.data.domain.TransactionModel
import com.example.ecommerceapp.data.network.response.EcommerceResponse
import com.example.ecommerceapp.repository.payment.PaymentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTransactionUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) {
    operator fun invoke() : Flow<EcommerceResponse<List<TransactionModel>>>{
        return paymentRepository.getTransaction()
    }
}
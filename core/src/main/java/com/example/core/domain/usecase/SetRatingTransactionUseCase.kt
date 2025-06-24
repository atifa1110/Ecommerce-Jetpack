package com.example.core.domain.usecase

import com.example.core.data.network.response.EcommerceResponse
import com.example.core.domain.repository.payment.PaymentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SetRatingTransactionUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) {

    operator fun invoke(invoiceId: String, rating: Int, review: String) : Flow<EcommerceResponse<String>> {
        return paymentRepository.setRatingTransaction(invoiceId,rating,review)
    }
}
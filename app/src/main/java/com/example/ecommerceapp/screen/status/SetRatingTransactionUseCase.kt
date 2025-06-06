package com.example.ecommerceapp.screen.status

import com.example.ecommerceapp.data.network.response.EcommerceResponse
import com.example.ecommerceapp.repository.payment.PaymentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SetRatingTransactionUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) {

    operator fun invoke(invoiceId: String, rating: Int, review: String) : Flow<EcommerceResponse<String>>{
        return paymentRepository.setRatingTransaction(invoiceId,rating,review)
    }
}
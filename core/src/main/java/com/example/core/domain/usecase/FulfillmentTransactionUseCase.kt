package com.example.core.domain.usecase

import com.example.core.data.network.response.EcommerceResponse
import com.example.core.domain.model.FulfillmentModel
import com.example.core.domain.model.ItemTransactionModel
import com.example.core.domain.repository.payment.PaymentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FulfillmentTransactionUseCase @Inject constructor(
  private val paymentRepository: PaymentRepository
) {
  operator fun invoke(payment: String, items : List<ItemTransactionModel>) : Flow<EcommerceResponse<FulfillmentModel>>{
    return paymentRepository.fulfillmentTransaction(payment,items)
  }
}
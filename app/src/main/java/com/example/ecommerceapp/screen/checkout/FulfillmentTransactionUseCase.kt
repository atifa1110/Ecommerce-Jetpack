package com.example.ecommerceapp.screen.checkout

import com.example.ecommerceapp.data.domain.FulfillmentModel
import com.example.ecommerceapp.data.domain.ItemTransactionModel
import com.example.ecommerceapp.data.network.response.EcommerceResponse
import com.example.ecommerceapp.repository.payment.PaymentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FulfillmentTransactionUseCase @Inject constructor(
  private val paymentRepository: PaymentRepository
) {
  operator fun invoke(payment: String, items : List<ItemTransactionModel>) : Flow<EcommerceResponse<FulfillmentModel>>{
    return paymentRepository.fulfillmentTransaction(payment,items)
  }
}
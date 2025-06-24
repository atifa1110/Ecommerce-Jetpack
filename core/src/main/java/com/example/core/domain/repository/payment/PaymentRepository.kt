package com.example.core.domain.repository.payment

import com.example.core.data.network.response.EcommerceResponse
import com.example.core.domain.model.FulfillmentModel
import com.example.core.domain.model.ItemTransactionModel
import com.example.core.domain.model.PaymentModel
import com.example.core.domain.model.TransactionModel
import kotlinx.coroutines.flow.Flow

interface PaymentRepository {
    fun getTransaction() : Flow<EcommerceResponse<List<TransactionModel>>>
    fun fulfillmentTransaction(payment : String , items : List<ItemTransactionModel>) : Flow<EcommerceResponse<FulfillmentModel>>
    fun setRatingTransaction(invoiceId : String, rating: Int, review: String) : Flow<EcommerceResponse<String>>
    fun getPaymentConfig() : Flow<EcommerceResponse<List<PaymentModel>>>
}
package com.example.ecommerceapp.repository.payment

import com.example.ecommerceapp.data.network.response.EcommerceResponse
import com.example.ecommerceapp.data.domain.FulfillmentModel
import com.example.ecommerceapp.data.domain.ItemTransactionModel
import com.example.ecommerceapp.data.domain.PaymentModel
import com.example.ecommerceapp.data.domain.TransactionModel
import kotlinx.coroutines.flow.Flow

interface PaymentRepository {
    fun getTransaction() : Flow<EcommerceResponse<List<TransactionModel>>>
    fun fulfillmentTransaction(payment : String , items : List<ItemTransactionModel>) : Flow<EcommerceResponse<FulfillmentModel>>
    fun setRatingTransaction(invoiceId : String, rating: Int, review: String) : Flow<EcommerceResponse<String>>
    fun getPaymentConfig() : Flow<EcommerceResponse<List<PaymentModel>>>
}
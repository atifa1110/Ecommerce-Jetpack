package com.example.core.data.repository.payment

import com.example.core.domain.model.FulfillmentModel
import com.example.core.domain.model.ItemTransactionModel
import com.example.core.domain.model.PaymentModel
import com.example.core.domain.model.TransactionModel
import com.example.core.data.mapper.asFulfillmentModel
import com.example.core.data.mapper.asItemTransactionNetwork
import com.example.core.data.mapper.asPaymentModel
import com.example.core.data.mapper.asTransactionModel
import com.example.core.data.network.datasource.PaymentNetworkDataSource
import com.example.core.data.network.datasource.RemoteConfigDataSource
import com.example.core.data.network.request.FulfillmentRequest
import com.example.core.data.network.request.RatingRequest
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.domain.repository.payment.PaymentRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
    private val paymentNetworkDataSource: PaymentNetworkDataSource,
    private val remoteConfigDataSource: RemoteConfigDataSource
) : PaymentRepository {
    override fun getTransaction(): Flow<EcommerceResponse<List<TransactionModel>>> {
        return flow {
            emit(EcommerceResponse.Loading)
            delay(1500L)
            when (val result = paymentNetworkDataSource.transaction()) {
                is EcommerceResponse.Success -> emit(EcommerceResponse.Success(result.value.data.map { it.asTransactionModel() }))
                is EcommerceResponse.Failure -> emit(EcommerceResponse.Failure(result.code, result.error))
                else->{}
            }
        }
    }


    override fun fulfillmentTransaction(payment : String , items : List<ItemTransactionModel>): Flow<EcommerceResponse<FulfillmentModel>> {
        return flow {
            emit(EcommerceResponse.Loading)
            delay(1500L)
            val request = FulfillmentRequest(payment, items.asItemTransactionNetwork())
            when (val result = paymentNetworkDataSource.fulfillment(request)) {
                is EcommerceResponse.Success -> emit(EcommerceResponse.Success(result.value.data.asFulfillmentModel()))
                is EcommerceResponse.Failure -> emit(EcommerceResponse.Failure(result.code, result.error))
                else->{}
            }
        }
    }

    override fun setRatingTransaction(invoiceId : String, rating: Int, review: String): Flow<EcommerceResponse<String>> {
        return flow {
            emit(EcommerceResponse.Loading)
            delay(2000L)
            val request = RatingRequest(invoiceId,rating,review)
            when (val result = paymentNetworkDataSource.rating(request)) {
                is EcommerceResponse.Success -> emit(EcommerceResponse.Success(result.value.message))
                is EcommerceResponse.Failure -> emit(EcommerceResponse.Failure(result.code, result.error))
                else->{}
            }
        }
    }

    override fun getPaymentConfig(): Flow<EcommerceResponse<List<PaymentModel>>> {
        return flow {
            emit(EcommerceResponse.Loading)
            delay(1500L)
            when (val result = remoteConfigDataSource.getPaymentConfig()){
                is EcommerceResponse.Failure -> emit(EcommerceResponse.Failure(result.code,result.error))
                is EcommerceResponse.Success-> {
                    emit(EcommerceResponse.Success(result.value.data.asPaymentModel()))
                }
                else -> {}
            }
        }
    }


}
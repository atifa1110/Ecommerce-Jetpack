package com.example.ecommerceapp.repository.payment

import com.example.ecommerceapp.data.domain.FulfillmentModel
import com.example.ecommerceapp.data.domain.ItemTransactionModel
import com.example.ecommerceapp.data.domain.PaymentModel
import com.example.ecommerceapp.data.domain.TransactionModel
import com.example.ecommerceapp.data.domain.mapper.asFulfillmentModel
import com.example.ecommerceapp.data.domain.mapper.asItemTransactionNetwork
import com.example.ecommerceapp.data.domain.mapper.asPaymentModel
import com.example.ecommerceapp.data.domain.mapper.asTransactionModel
import com.example.ecommerceapp.data.network.datasource.PaymentNetworkDataSource
import com.example.ecommerceapp.data.network.datasource.RemoteConfigDataSource
import com.example.ecommerceapp.data.network.request.FulfillmentRequest
import com.example.ecommerceapp.data.network.request.RatingRequest
import com.example.ecommerceapp.data.network.response.EcommerceResponse
import com.example.ecommerceapp.ui.theme.EcommerceAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
    private val paymentNetworkDataSource: PaymentNetworkDataSource,
    private val remoteConfigDataSource: RemoteConfigDataSource
) : PaymentRepository{
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
            delay(2000L)
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
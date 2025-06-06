package com.example.ecommerceapp.data.network.datasource

import com.example.ecommerceapp.data.network.api.PaymentApiService
import com.example.ecommerceapp.data.network.request.FulfillmentRequest
import com.example.ecommerceapp.data.network.request.RatingRequest
import com.example.ecommerceapp.data.network.response.EcommerceResponse
import com.example.ecommerceapp.data.network.response.FulfillmentResponse
import com.example.ecommerceapp.data.network.response.RatingResponse
import com.example.ecommerceapp.data.network.response.TransactionResponse
import com.example.ecommerceapp.data.network.utils.BaseRemoteDataSource
import com.google.gson.Gson
import javax.inject.Inject

class PaymentNetworkDataSource @Inject constructor(
    private val paymentApiService: PaymentApiService,
    gson : Gson
): BaseRemoteDataSource(gson){

    suspend fun fulfillment(request: FulfillmentRequest): EcommerceResponse<FulfillmentResponse> {
        return safeApiCall{ paymentApiService.fulfillment(request) }
    }
    suspend fun transaction(): EcommerceResponse<TransactionResponse> {
        return safeApiCall{ paymentApiService.transaction() }
    }

    suspend fun rating(request: RatingRequest): EcommerceResponse<RatingResponse> {
        return safeApiCall{ paymentApiService.rating(request) }
    }
}
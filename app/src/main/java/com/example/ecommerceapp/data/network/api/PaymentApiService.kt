package com.example.ecommerceapp.data.network.api

import com.example.ecommerceapp.data.network.request.FulfillmentRequest
import com.example.ecommerceapp.data.network.request.RatingRequest
import com.example.ecommerceapp.data.network.response.FulfillmentResponse
import com.example.ecommerceapp.data.network.response.RatingResponse
import com.example.ecommerceapp.data.network.response.TransactionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PaymentApiService {
    @POST("fulfillment")
    suspend fun fulfillment(
        @Body request: FulfillmentRequest
    ) : Response<FulfillmentResponse>

    @GET("transaction")
    suspend fun transaction() : Response<TransactionResponse>

    @POST("rating")
    suspend fun rating(
        @Body request : RatingRequest
    ) : Response<RatingResponse>
}
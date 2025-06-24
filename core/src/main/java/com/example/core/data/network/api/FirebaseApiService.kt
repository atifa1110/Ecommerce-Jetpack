package com.example.core.data.network.api

import com.example.core.data.network.request.TokenRequest
import com.example.core.data.network.response.BaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FirebaseApiService {
    @POST("firebase")
    suspend fun firebaseToken(
        @Body request : TokenRequest
    ): Response<BaseResponse>
}
package com.example.core.data.network.datasource

import com.example.core.data.network.api.FirebaseApiService
import com.example.core.data.network.request.TokenRequest
import com.example.core.data.network.response.BaseResponse
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.data.network.utils.BaseRemoteDataSource
import com.google.gson.Gson
import javax.inject.Inject

class FirebaseNetworkDataSource @Inject constructor(
    private val firebaseApiService: FirebaseApiService,
    private val gson : Gson
) : BaseRemoteDataSource(gson) {

    suspend fun updateFCMToken(request : TokenRequest): EcommerceResponse<BaseResponse> {
        return safeApiCall { firebaseApiService.firebaseToken(request)}
    }
}
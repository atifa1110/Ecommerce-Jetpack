package com.example.ecommerceapp.data.network.datasource

import com.example.ecommerceapp.data.network.api.AuthApiService
import com.example.ecommerceapp.data.network.request.AuthRequest
import com.example.ecommerceapp.data.network.request.TokenRequest
import com.example.ecommerceapp.data.network.response.EcommerceResponse
import com.example.ecommerceapp.data.network.response.LoginResponse
import com.example.ecommerceapp.data.network.response.ProfileResponse
import com.example.ecommerceapp.data.network.response.RefreshResponse
import com.example.ecommerceapp.data.network.response.RegisterResponse
import com.example.ecommerceapp.data.network.utils.BaseRemoteDataSource
import com.google.gson.Gson
import okhttp3.MultipartBody
import javax.inject.Inject

class AuthNetworkDataSource @Inject constructor(
    private val authApiService: AuthApiService,
    private val gson : Gson
): BaseRemoteDataSource(gson){

    suspend fun loginUser(request: AuthRequest): EcommerceResponse<LoginResponse> {
        return safeApiCall{ authApiService.loginUser(request)}
    }

    suspend fun registerUser(request: AuthRequest): EcommerceResponse<RegisterResponse> {
        return safeApiCall { authApiService.registerUser(request) }
    }

    suspend fun refreshToken(tokenRequest: TokenRequest): EcommerceResponse<RefreshResponse> {
        return safeApiCall { authApiService.refreshToken(tokenRequest) }
    }

    suspend fun profileUser(
        userImage: MultipartBody.Part,
        userName: MultipartBody.Part
    ): EcommerceResponse<ProfileResponse> {
        return safeApiCall { authApiService.profileUser(userName = userName, userImage = userImage) }
    }
}
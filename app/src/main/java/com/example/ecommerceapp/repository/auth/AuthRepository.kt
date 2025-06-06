package com.example.ecommerceapp.repository.auth

import com.example.ecommerceapp.data.network.request.AuthRequest
import com.example.ecommerceapp.data.network.request.TokenRequest
import com.example.ecommerceapp.data.network.response.EcommerceResponse
import com.example.ecommerceapp.data.network.response.LoginResponse
import com.example.ecommerceapp.data.network.response.RegisterResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface AuthRepository {
    fun login(request: AuthRequest): Flow<EcommerceResponse<String>>
    fun register(request: AuthRequest): Flow<EcommerceResponse<String>>
    fun profile(userImage: MultipartBody.Part,userName: MultipartBody.Part): Flow<EcommerceResponse<String>>
    fun refresh(request: TokenRequest): Flow<EcommerceResponse<String>>
}
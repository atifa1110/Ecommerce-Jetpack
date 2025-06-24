package com.example.core.domain.repository.auth

import com.example.core.data.network.request.AuthRequest
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.data.network.request.TokenRequest
import com.example.core.domain.model.UserModel
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface AuthRepository {
    fun login(request: AuthRequest): Flow<EcommerceResponse<UserModel>>
    fun register(request: AuthRequest): Flow<EcommerceResponse<String>>
    fun profile(userImage: MultipartBody.Part,userName: MultipartBody.Part): Flow<EcommerceResponse<String>>
    fun refresh(request: TokenRequest): Flow<EcommerceResponse<String>>
}
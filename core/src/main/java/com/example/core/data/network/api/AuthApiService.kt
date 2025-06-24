package com.example.core.data.network.api

import com.example.core.data.network.request.AuthRequest
import com.example.core.data.network.request.TokenRequest
import com.example.core.data.network.response.LoginResponse
import com.example.core.data.network.response.ProfileResponse
import com.example.core.data.network.response.RefreshResponse
import com.example.core.data.network.response.RegisterResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AuthApiService {
    @Headers("No-Auth: true")
    @POST("login")
    suspend fun loginUser(
        @Body loginRequest: AuthRequest
    ): Response<LoginResponse>

    @Headers("No-Auth: true")
    @POST("register")
    suspend fun registerUser(
        @Body registerRequest: AuthRequest
    ): Response<RegisterResponse>

    @POST("refresh")
    suspend fun refreshToken(
        @Body token: TokenRequest
    ): Response<RefreshResponse>

    @Multipart
    @POST("profile")
    suspend fun profileUser (
        @Part userImage : MultipartBody.Part,
        @Part userName : MultipartBody.Part
    ) : Response<ProfileResponse>
}
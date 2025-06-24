package com.example.ecommerceapp.di

import com.example.ecommerceapp.BuildConfig
import com.example.core.data.repository.token.TokenRepositoryImpl
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class HeaderInterceptor @Inject constructor(
    private val tokenRepositoryImpl: TokenRepositoryImpl
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = runBlocking { tokenRepositoryImpl.getToken() }

        // Try request with token if available
        val requestWithAuth = originalRequest.newBuilder().apply {
            if (!token.isNullOrEmpty()) {
                addHeader("Authorization", "Bearer $token")
            } else {
                addHeader("API_KEY", BuildConfig.API_KEY)
            }
        }.build()

        var response = chain.proceed(requestWithAuth)

        // If response is 401 and token was used, retry with API_KEY
        if (response.code == 403 && !token.isNullOrEmpty()) {
            response.close() // Important: close previous response
            val requestWithApiKey = originalRequest.newBuilder()
                .removeHeader("Authorization")
                .addHeader("API_KEY", BuildConfig.API_KEY)
                .build()

            response = chain.proceed(requestWithApiKey)
        }

        return response
    }
}

package com.example.ecommerceapp.di

import android.util.Log
import com.example.ecommerceapp.repository.token.TokenRepositoryImpl
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class HeaderInterceptor @Inject constructor(
    private val tokenRepositoryImpl: TokenRepositoryImpl
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        val token = runBlocking { tokenRepositoryImpl.getToken() }
        //val token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJlY29tbWVyY2UtYXVkaWVuY2UiLCJpc3MiOiJodHRwOi8vMTkyLjE2OC4xMDAuMTM6NTAwMC8iLCJ1c2VySWQiOiIzY2RhNzg3Ny1hNzU3LTQ3NTQtYTVmOC0zMmVkYjIwZmZhNmIiLCJ0b2tlblR5cGUiOiJhY2Nlc3NUb2tlbiIsImV4cCI6MTc0ODcwODQxMn0.YBJIudsN7vgd8cyRCCIgIchvTefikHBqekVXrkJwQ4jFiQDuGME95twVneKoIGM2zfLlh7W566pzJFi5NtLgm"

        if (token.isNullOrEmpty()) {
            requestBuilder.addHeader("API_KEY", "6f8856ed-9189-488f-9011-0ff4b6c08edc")
        } else {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}

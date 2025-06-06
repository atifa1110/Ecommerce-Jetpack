package com.example.ecommerceapp.repository.token

interface TokenRepository {
    suspend fun setToken(token : String)
    suspend fun setRefresh(token : String)
    suspend fun getToken(): String?
    suspend fun clearAuthToken()
}
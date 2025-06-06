package com.example.ecommerceapp.repository.user

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserName() : Flow<String>
    fun getUserImage() : Flow<String>
    suspend fun clearUserData()
}
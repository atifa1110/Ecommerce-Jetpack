package com.example.core.domain.repository.messaging

import com.example.core.data.network.response.EcommerceResponse
import kotlinx.coroutines.flow.Flow

interface MessagingRepository {
    suspend fun getFirebasePhoneToken(): String
    fun subscribeFcmTopic(): Boolean
    suspend fun updateFcmToken() : Flow<EcommerceResponse<String>>
}
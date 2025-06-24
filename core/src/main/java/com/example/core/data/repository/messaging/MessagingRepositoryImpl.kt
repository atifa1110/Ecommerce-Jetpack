package com.example.core.data.repository.messaging

import com.example.core.BuildConfig
import com.example.core.data.network.datasource.FirebaseNetworkDataSource
import com.example.core.domain.repository.messaging.MessagingRepository
import com.example.core.data.network.request.TokenRequest
import com.example.core.data.network.response.EcommerceResponse
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MessagingRepositoryImpl @Inject constructor(
    private val firebaseNetworkDataSource: FirebaseNetworkDataSource
): MessagingRepository {

    override suspend fun getFirebasePhoneToken(): String {
        return FirebaseMessaging.getInstance().token.await()
    }

    override fun subscribeFcmTopic(): Boolean {
        return FirebaseMessaging.getInstance().subscribeToTopic("promo").isSuccessful
    }

    override suspend fun updateFcmToken(): Flow<EcommerceResponse<String>> = flow{
        emit(EcommerceResponse.Loading)

        val request = TokenRequest(BuildConfig.AUTH_KEY)
        val response = firebaseNetworkDataSource.updateFCMToken(request)
        when(response) {
            is EcommerceResponse.Failure -> emit(EcommerceResponse.Failure(response.code,response.error))
            is EcommerceResponse.Success -> emit(EcommerceResponse.Success("Update is success"))
            else -> {}
        }
    }
}
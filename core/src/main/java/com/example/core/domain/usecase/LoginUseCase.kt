package com.example.core.domain.usecase

import com.example.core.domain.model.UserModel
import com.example.core.data.network.request.AuthRequest
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.domain.repository.auth.AuthRepository
import com.example.core.domain.repository.messaging.MessagingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val messagingRepository: MessagingRepository
) {
    operator fun invoke(email: String, password: String): Flow<EcommerceResponse<UserModel>> =
        flow {
            emit(EcommerceResponse.Loading)  // emit loading first

            try {
                messagingRepository.subscribeFcmTopic()
                val firebaseToken = messagingRepository.getFirebasePhoneToken()
                val request = AuthRequest(email, password, firebaseToken)

                // collect response from authRepository.login and emit it downstream
                authRepository.login(request).collect { response ->
                    emit(response)
                }
            } catch (e: Exception) {
                emit(EcommerceResponse.Failure(-1, e.message ?: "Unknown error"))
            }
        }
}
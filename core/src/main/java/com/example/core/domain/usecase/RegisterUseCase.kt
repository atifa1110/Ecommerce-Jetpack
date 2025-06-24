package com.example.core.domain.usecase

import com.example.core.data.network.request.AuthRequest
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.domain.repository.auth.AuthRepository
import com.example.core.domain.repository.messaging.MessagingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val messagingRepository: MessagingRepository
) {
    operator fun invoke(email: String, password: String): Flow<EcommerceResponse<String>> = flow {
        emit(EcommerceResponse.Loading)  // emit loading first

        try {
            messagingRepository.subscribeFcmTopic()
            val firebaseToken =
                messagingRepository.getFirebasePhoneToken()  // suspend call allowed inside flow builder
            val request = AuthRequest(email, password, firebaseToken)

            // collect response from authRepository.register and emit it downstream
            authRepository.register(request).collect { response ->
                emit(response)
            }
        } catch (e: Exception) {
            emit(EcommerceResponse.Failure(-1, e.message ?: "Unknown error"))
        }
    }
}
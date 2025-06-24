package com.example.core.domain.usecase

import com.example.core.data.network.response.EcommerceResponse
import com.example.core.domain.repository.messaging.MessagingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateFcmTokenUseCase @Inject constructor(
    private val messageRepository: MessagingRepository
) {
    suspend operator fun invoke() : Flow<EcommerceResponse<String>> {
        return messageRepository.updateFcmToken()
    }
}
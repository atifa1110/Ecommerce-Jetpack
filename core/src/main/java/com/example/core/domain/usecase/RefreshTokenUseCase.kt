package com.example.core.domain.usecase

import android.util.Log
import com.example.core.data.network.request.TokenRequest
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.domain.repository.auth.AuthRepository
import com.example.core.domain.repository.token.TokenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class RefreshTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke(): Flow<EcommerceResponse<String>> {
        val token = tokenRepository.getRefresh()
        return if (token == null) {
            flowOf(EcommerceResponse.Failure(-1,"No refresh token found"))
        } else {
            val request = TokenRequest(token)
            authRepository.refresh(request)
        }
    }

}

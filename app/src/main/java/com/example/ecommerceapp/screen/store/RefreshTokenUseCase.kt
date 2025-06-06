package com.example.ecommerceapp.screen.store

import com.example.ecommerceapp.data.network.request.TokenRequest
import com.example.ecommerceapp.data.network.response.EcommerceResponse
import com.example.ecommerceapp.repository.auth.AuthRepository
import com.example.ecommerceapp.repository.token.TokenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class RefreshTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke(): Flow<EcommerceResponse<String>> {
        val token = tokenRepository.getToken()?:""
        val request = TokenRequest(token)
        return authRepository.refresh(request)
    }
}

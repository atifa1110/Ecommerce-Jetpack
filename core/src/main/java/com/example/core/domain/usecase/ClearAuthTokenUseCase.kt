package com.example.core.domain.usecase

import com.example.core.domain.repository.token.TokenRepository
import javax.inject.Inject

class ClearAuthTokenUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke() {
        return tokenRepository.clearAuthToken()
    }
}

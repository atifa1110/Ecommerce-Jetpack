package com.example.ecommerceapp.screen.home

import com.example.ecommerceapp.repository.token.TokenRepository
import javax.inject.Inject

class ClearAuthTokenUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke() {
        return tokenRepository.clearAuthToken()
    }
}

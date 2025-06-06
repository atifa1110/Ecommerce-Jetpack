package com.example.ecommerceapp.screen.login

import com.example.ecommerceapp.data.network.request.AuthRequest
import com.example.ecommerceapp.data.network.response.EcommerceResponse
import com.example.ecommerceapp.repository.auth.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(request: AuthRequest): Flow<EcommerceResponse<String>> {
        return authRepository.login(request)
    }
}

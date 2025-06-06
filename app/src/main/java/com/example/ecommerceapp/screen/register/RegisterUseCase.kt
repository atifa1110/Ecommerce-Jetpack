package com.example.ecommerceapp.screen.register

import com.example.ecommerceapp.data.network.request.AuthRequest
import com.example.ecommerceapp.data.network.response.EcommerceResponse
import com.example.ecommerceapp.repository.auth.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(request: AuthRequest): Flow<EcommerceResponse<String>> {
        return authRepository.register(request)
    }
}

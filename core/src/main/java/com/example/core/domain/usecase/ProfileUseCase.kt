package com.example.core.domain.usecase

import com.example.core.data.network.response.EcommerceResponse
import com.example.core.domain.repository.auth.AuthRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import javax.inject.Inject

class ProfileUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(userImage: MultipartBody.Part, userName: MultipartBody.Part): Flow<EcommerceResponse<String>> {
        return authRepository.profile(userImage,userName)
    }
}
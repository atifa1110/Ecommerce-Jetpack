package com.example.ecommerceapp.screen.profile

import com.example.ecommerceapp.data.network.response.EcommerceResponse
import com.example.ecommerceapp.repository.auth.AuthRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import javax.inject.Inject

class ProfileUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(userImage: MultipartBody.Part,userName: MultipartBody.Part): Flow<EcommerceResponse<String>> {
        return authRepository.profile(userImage,userName)
    }
}
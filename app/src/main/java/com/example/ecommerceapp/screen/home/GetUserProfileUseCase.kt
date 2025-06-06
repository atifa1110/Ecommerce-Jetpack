package com.example.ecommerceapp.screen.home

import com.example.ecommerceapp.repository.user.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke() : Flow<String> {
        return userRepository.getUserImage()
    }
}

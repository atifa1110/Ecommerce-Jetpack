package com.example.ecommerceapp.screen.home

import com.example.ecommerceapp.repository.token.TokenRepository
import com.example.ecommerceapp.repository.user.UserRepository
import javax.inject.Inject

class ClearUserDataUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() {
        return userRepository.clearUserData()
    }
}

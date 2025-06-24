package com.example.core.domain.usecase

import com.example.core.domain.repository.user.UserRepository
import javax.inject.Inject

class ClearUserDataUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() {
        return userRepository.clearUserData()
    }
}

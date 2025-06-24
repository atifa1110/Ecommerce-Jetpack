package com.example.core.domain.usecase

import com.example.core.domain.repository.user.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserNameUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke() : Flow<String> {
        return userRepository.getUserName()
    }
}

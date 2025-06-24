package com.example.core.domain.usecase

import com.example.core.domain.repository.state.StateRepository
import javax.inject.Inject

class SetProfileStateUseCase @Inject constructor(
    private val stateRepository: StateRepository
) {
    suspend operator fun invoke(complete: Boolean) {
        return stateRepository.setProfile(complete)
    }
}
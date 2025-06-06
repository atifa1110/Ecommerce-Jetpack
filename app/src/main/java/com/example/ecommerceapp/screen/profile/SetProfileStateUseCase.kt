package com.example.ecommerceapp.screen.profile

import com.example.ecommerceapp.repository.state.StateRepository
import javax.inject.Inject

class SetProfileStateUseCase @Inject constructor(
    private val stateRepository: StateRepository
) {
    suspend operator fun invoke(complete: Boolean) {
        return stateRepository.setProfile(complete)
    }
}

package com.example.ecommerceapp.screen.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.network.response.EcommerceResponse
import com.example.ecommerceapp.firebase.AuthAnalytics
import com.example.core.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterUiState(
    val email: String = "",
    val emailError: String? = "",
    val password: String = "",
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
)

sealed class RegisterEvent {
    data class ShowSnackbar(val message: String) : RegisterEvent()
}

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val authAnalytics: AuthAnalytics
): ViewModel(){

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    private val _eventFlow = MutableSharedFlow<RegisterEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    val isFormValid: Boolean
        get() = uiState.value.emailError == null && uiState.value.passwordError == null
                && uiState.value.email.isNotBlank() && uiState.value.password.isNotBlank()

    fun onEmailChange(newEmail: String) {
        val emailRegex = "^[A-Za-z0-9+_.-]+@(.+)\$"
        val error = when {
            newEmail.isBlank() -> "Email cannot be empty"
            !newEmail.matches(emailRegex.toRegex())-> "Invalid email format"
            else -> null
        }
        _uiState.update {
            it.copy(email = newEmail, emailError = error)
        }
    }

    fun onPasswordChange(newPassword: String) {
        val error = when {
            newPassword.isBlank() -> "Password cannot be empty"
            newPassword.length < 8 -> "Password must be at least 8 characters"
            else -> null
        }
        _uiState.update {
            it.copy(password = newPassword, passwordError = error)
        }
    }

    fun registerEmailAndPassword() = viewModelScope.launch {
        authAnalytics.trackSignUpButtonClicked()
        authAnalytics.trackSignUpAttempt(
            uiState.value.email,
            uiState.value.password.isNotBlank(),
            uiState.value.passwordError == null
        )
        registerUseCase.invoke(uiState.value.email,uiState.value.password).collect { result ->
            when (result) {
                is EcommerceResponse.Failure -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = false,
                        )
                    }
                    _eventFlow.emit(RegisterEvent.ShowSnackbar(result.error))
                    authAnalytics.trackSignUpFailure(result.error, uiState.value.email)
                }

                EcommerceResponse.Loading -> {
                    _uiState.update {
                        it.copy(
                            isLoading = true,
                        )
                    }
                }

                is EcommerceResponse.Success -> {
                    _uiState.update {
                        it.copy(
                            isSuccess = true,
                            isLoading = false,
                        )
                    }
                    _eventFlow.emit(RegisterEvent.ShowSnackbar("Register is Success"))
                    authAnalytics.trackSignUpSuccess(result.value.isNotEmpty())
                }
            }
        }
    }
}
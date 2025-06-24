package com.example.ecommerceapp.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.usecase.GetBoardingStateUseCase
import com.example.core.domain.usecase.GetDarkStateUseCase
import com.example.core.domain.usecase.GetLoginStateUseCase
import com.example.core.domain.usecase.GetProfileStateUseCase
import com.example.core.domain.usecase.GetRegisterStateUseCase
import com.example.core.domain.usecase.SetDarkStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getBoardingStateUseCase: GetBoardingStateUseCase,
    private val getRegisterStateUseCase: GetRegisterStateUseCase,
    private val getProfileStateUseCase: GetProfileStateUseCase,
    private val getLoginStateUseCase: GetLoginStateUseCase,
    private val getDarkStateUseCase: GetDarkStateUseCase,
    private val setDarkStateUseCase: SetDarkStateUseCase,
) : ViewModel(){

    val onBoardingState = getBoardingStateUseCase()
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)
    val onLoginState = getLoginStateUseCase()
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)
    val onProfileState = getProfileStateUseCase()
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)
    val onRegisterStateUseCase = getRegisterStateUseCase()
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)
    val isDarkMode = getDarkStateUseCase()
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            setDarkStateUseCase.invoke(enabled)
        }
    }

    private val _isLoadingComplete = MutableStateFlow(false)
    val isLoadingComplete: StateFlow<Boolean> = _isLoadingComplete.asStateFlow()

    init {
        viewModelScope.launch {
            delay(100L)
            _isLoadingComplete.value = true
        }
    }
}
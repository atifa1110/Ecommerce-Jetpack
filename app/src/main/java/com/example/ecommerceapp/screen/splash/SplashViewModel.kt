package com.example.ecommerceapp.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.screen.home.ClearAppStateUseCase
import com.example.ecommerceapp.screen.home.ClearAuthTokenUseCase
import com.example.ecommerceapp.screen.home.ClearUserDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getBoardingStateUseCase: GetBoardingStateUseCase,
    private val getRegisterStateUseCase: GetRegisterStateUseCase,
    private val getProfileStateUseCase: GetProfileStateUseCase,
    private val getLoginStateUseCase: GetLoginStateUseCase,
    private val clearUserDataUseCase: ClearUserDataUseCase,
    private val clearAuthTokenUseCase: ClearAuthTokenUseCase,
    private val clearAppStateUseCase: ClearAppStateUseCase
) : ViewModel(){

    val onBoardingState = getBoardingStateUseCase.invoke()
    val onLoginState = getLoginStateUseCase.invoke()
    val onProfileState = getProfileStateUseCase.invoke()
    val onRegisterStateUseCase = getRegisterStateUseCase.invoke()

    fun clearUserData () = viewModelScope.launch {
        clearUserDataUseCase.invoke()
        clearAuthTokenUseCase.invoke()
        clearAppStateUseCase.invoke()
    }
}
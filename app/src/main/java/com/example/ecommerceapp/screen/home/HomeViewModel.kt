package com.example.ecommerceapp.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val clearAuthTokenUseCase: ClearAuthTokenUseCase,
    private val clearAppStateUseCase: ClearAppStateUseCase,
    private val clearUserDataUseCase: ClearUserDataUseCase,
    private val setDarkStateUseCase: SetDarkStateUseCase,
    private val getDarkStateUseCase: GetDarkStateUseCase
) : ViewModel(){

    val isDarkMode = getDarkStateUseCase.invoke()

    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            setDarkStateUseCase.invoke(enabled)
        }
    }

    fun onLogoutClick () = viewModelScope.launch {
        clearAppStateUseCase.invoke()
        clearUserDataUseCase.invoke()
        clearAuthTokenUseCase.invoke()
    }

}
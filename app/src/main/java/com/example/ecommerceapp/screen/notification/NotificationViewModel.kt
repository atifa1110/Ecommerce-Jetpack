package com.example.ecommerceapp.screen.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.ui.model.Notification
import com.example.core.ui.mapper.asNotification
import com.example.core.domain.usecase.GetNotificationUseCase
import com.example.core.domain.usecase.UpdateNotificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NotificationUiState(
    val isLoading : Boolean = false,
    val isError: Boolean = false,
    val notifications : List<Notification>  = emptyList()
)

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val getNotificationUseCase: GetNotificationUseCase,
    private val updateNotificationUseCase: UpdateNotificationUseCase
) : ViewModel(){

    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState: StateFlow<NotificationUiState> = _uiState

    init {
        getNotification()
    }

    fun getNotification() = viewModelScope.launch {
        getNotificationUseCase.invoke().collect { result ->
            when(result){
                is EcommerceResponse.Failure -> {
                    _uiState.update {
                        it.copy(isLoading= false,isError = true)
                    }
                }
                EcommerceResponse.Loading -> {
                    _uiState.update {
                        it.copy(isLoading = true,isError = false)
                    }
                }
                is EcommerceResponse.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isError = false,
                            notifications = result.value.asNotification())
                    }
                }
            }
        }
    }

    fun updateNotification(id: Int, read: Boolean) = viewModelScope.launch {
        updateNotificationUseCase.invoke(id,read)
        _uiState.update { state ->
            val updated = state.notifications.map {
                if (it.id == id && !it.isRead) it.copy(isRead = true) else it
            }
            state.copy(notifications = updated)
        }
    }
}
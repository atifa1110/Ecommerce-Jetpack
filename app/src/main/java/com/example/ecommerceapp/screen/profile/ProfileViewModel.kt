package com.example.ecommerceapp.screen.profile

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.network.response.EcommerceResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

data class ProfileUiState(
    val imageUri: Uri? = null,
    val showImageDialog: Boolean = false,
    val userImage: File? = null,
    val userName : String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val userMessage: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileUseCase: ProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    fun setImageUri(uri: Uri?) {
        _uiState.update { it.copy(imageUri = uri) }
    }

    fun setUserImage(file: File?) {
        _uiState.update { it.copy(userImage = file) }
    }

    fun setUserName(name: String) {
        _uiState.update { it.copy(userName = name) }
    }

    fun showDialog() {
        _uiState.update { it.copy(showImageDialog = true) }
    }

    fun hideDialog() {
        _uiState.update { it.copy(showImageDialog = false) }
    }

    fun snackBarMessageShown(){
        _uiState.update {  it.copy(userMessage = null) }
    }

    fun createImageFileAndUri(context: Context): Uri {
        val file = File.createTempFile(
            "profile-${System.currentTimeMillis()}",
            ".jpg",
            context.externalCacheDir
        )

        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )

        _uiState.update {
            it.copy(imageUri = uri, userImage = file)
        }

        return uri
    }

    fun onSetProfileClick() = viewModelScope.launch {
        val file = uiState.value.userImage
        val username = uiState.value.userName

        if (file == null || username.isBlank()) {
            // Handle error (e.g., show toast or snackbar)
            _uiState.update { it.copy(userMessage = "Data is Empty") }
            return@launch
        }

        val imagePart = MultipartBody.Part.createFormData(
            "userImage", file.name, file.asRequestBody("image/*".toMediaType())
        )

        val usernamePart = MultipartBody.Part.createFormData("userName", username)

        profileUseCase.invoke(imagePart, usernamePart).collect { result ->
            when (result) {
                is EcommerceResponse.Failure -> {
                    _uiState.update {
                        it.copy(isLoading = false, isSuccess = false, userMessage = result.error)
                    }
                }
                is EcommerceResponse.Loading -> {
                    _uiState.update {
                        it.copy(isLoading = true)
                    }
                }
                is EcommerceResponse.Success -> {
                    _uiState.update {
                        it.copy(isLoading = false, isSuccess = true, userMessage = "Upload Is Success")
                    }
                }
            }
        }
    }
}

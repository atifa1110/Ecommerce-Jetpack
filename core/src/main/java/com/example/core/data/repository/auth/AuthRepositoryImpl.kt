package com.example.core.data.repository.auth

import com.example.core.domain.repository.auth.AuthRepository
import com.example.core.data.local.datastore.AppStateDataStore
import com.example.core.data.local.datastore.AuthTokenDataStore
import com.example.core.data.local.datastore.UserDataStore
import com.example.core.data.network.datasource.AuthNetworkDataSource
import com.example.core.data.network.request.AuthRequest
import com.example.core.data.network.request.TokenRequest
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.domain.model.UserModel
import com.example.core.data.mapper.asUser
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authNetworkDataSource: AuthNetworkDataSource,
    private val authTokenDataStore: AuthTokenDataStore,
    private val appStateDataStore: AppStateDataStore,
    private val userDataStore: UserDataStore
) : AuthRepository {

    override fun login(request: AuthRequest): Flow<EcommerceResponse<UserModel>> = flow {
        emit(EcommerceResponse.Loading)
        delay(1000L)

        val response = authNetworkDataSource.loginUser(request)
        when(response) {
            is EcommerceResponse.Success -> {
                // Save token and refresh token to DataStore
                authTokenDataStore.saveAccessToken(response.value.data.accessToken)
                authTokenDataStore.saveRefreshToken(response.value.data.refreshToken)
                authTokenDataStore.saveExpiresAt(response.value.data.expiresAt)
                appStateDataStore.saveLoginState(true)

                // Optional: save user info if needed
                userDataStore.saveUserName(response.value.data.userName)
                userDataStore.saveUserImage(response.value.data.userImage)

                emit(EcommerceResponse.Success(response.value.data.asUser()))
            }
            is EcommerceResponse.Failure -> {
                emit(EcommerceResponse.Failure(response.code,response.error))
            }
            else -> {}
        }
    }

    override fun register(request: AuthRequest): Flow<EcommerceResponse<String>> = flow{
        emit(EcommerceResponse.Loading)
        delay(1000L)

        val response = authNetworkDataSource.registerUser(request)

        when(response){
            is EcommerceResponse.Success -> {
                // Save token and refresh token to DataStore
                authTokenDataStore.saveAccessToken(response.value.data.accessToken)
                authTokenDataStore.saveRefreshToken(response.value.data.refreshToken)
                authTokenDataStore.saveExpiresAt(response.value.data.expiresAt)
                appStateDataStore.saveRegisterState(true)

                emit(EcommerceResponse.Success(response.value.data.accessToken))
            }
            is EcommerceResponse.Failure -> {
                emit(EcommerceResponse.Failure(response.code,response.error))
            }
            else -> {
            }
        }
    }

    override fun profile(userImage: MultipartBody.Part,userName: MultipartBody.Part): Flow<EcommerceResponse<String>> = flow {
        emit(EcommerceResponse.Loading)
        delay(1000L)
        val response = authNetworkDataSource.profileUser(userImage,userName)
        when(response){
            is EcommerceResponse.Success -> {
                // Optional: save user info if needed
                userDataStore.saveUserName(response.value.data.userName)
                userDataStore.saveUserImage(response.value.data.userImage)
                appStateDataStore.saveProfileState(true)
                appStateDataStore.saveLoginState(true)

                emit(EcommerceResponse.Success("Update Profile is Success"))
            }
            is EcommerceResponse.Failure -> {
                emit(EcommerceResponse.Failure(response.code,response.error))
            }
            else -> {}
        }
    }

    override fun refresh(request: TokenRequest): Flow<EcommerceResponse<String>> = flow{
        emit(EcommerceResponse.Loading)
        delay(500L)
        val response = authNetworkDataSource.refreshToken(request)
        when(response){
            is EcommerceResponse.Success -> {
                // Save token and refresh token to DataStore
                authTokenDataStore.saveAccessToken(response.value.data.accessToken)
                authTokenDataStore.saveRefreshToken(response.value.data.refreshToken)
                authTokenDataStore.saveExpiresAt(response.value.data.expiresAt)

                emit(EcommerceResponse.Success("Refresh reload success"))
            }
            is EcommerceResponse.Failure -> {
                emit(EcommerceResponse.Failure(response.code,response.error))
            }
            else -> {}
        }
    }

}

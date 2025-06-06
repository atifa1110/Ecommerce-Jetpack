package com.example.ecommerceapp.repository.auth

import com.example.ecommerceapp.data.local.datastore.AppStateDataStore
import com.example.ecommerceapp.data.local.datastore.AuthTokenDataStore
import com.example.ecommerceapp.data.local.datastore.UserDataStore
import com.example.ecommerceapp.data.network.datasource.AuthNetworkDataSource
import com.example.ecommerceapp.data.network.request.AuthRequest
import com.example.ecommerceapp.data.network.request.TokenRequest
import com.example.ecommerceapp.data.network.response.EcommerceResponse
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

    override fun login(request: AuthRequest): Flow<EcommerceResponse<String>> = flow {
        emit(EcommerceResponse.Loading)
        delay(2000L)

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
                emit(EcommerceResponse.Success("Login is Success"))
            }
            is EcommerceResponse.Failure -> {
                emit(EcommerceResponse.Failure(response.code,response.error))
            }
            else -> {}
        }
    }


    override fun register(request: AuthRequest): Flow<EcommerceResponse<String>> = flow{
        emit(EcommerceResponse.Loading)
        delay(2000L)

        val response = authNetworkDataSource.registerUser(request)

        when(response){
            is EcommerceResponse.Success -> {
                // Save token and refresh token to DataStore
                authTokenDataStore.saveAccessToken(response.value.data.accessToken)
                authTokenDataStore.saveRefreshToken(response.value.data.refreshToken)
                authTokenDataStore.saveExpiresAt(response.value.data.expiresAt)
                appStateDataStore.saveRegisterState(true)

                emit(EcommerceResponse.Success("Register is Success"))
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
            is EcommerceResponse.Loading -> {
                // This case generally should not happen here, but if it does:
                emit(EcommerceResponse.Loading)
            }
        }
    }

    override fun refresh(request: TokenRequest): Flow<EcommerceResponse<String>> = flow{
        emit(EcommerceResponse.Loading)
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
            is EcommerceResponse.Loading -> {
                // This case generally should not happen here, but if it does:
                emit(EcommerceResponse.Loading)
            }
        }
    }
}
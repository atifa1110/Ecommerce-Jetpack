package com.example.core.data.repository.state

import com.example.core.domain.repository.state.StateRepository
import com.example.core.data.local.datastore.AppStateDataStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StateRepositoryImpl @Inject constructor(
    private val appStateDataStore: AppStateDataStore
) : StateRepository {

    override suspend fun setLogin(complete: Boolean) {
        return appStateDataStore.saveLoginState(complete)
    }

    override suspend fun setRegister(complete: Boolean) {
        return appStateDataStore.saveRegisterState(complete)
    }

    override suspend fun setBoarding(complete: Boolean) {
        return appStateDataStore.saveOnBoardingState(complete)
    }

    override suspend fun setProfile(complete: Boolean) {
        return appStateDataStore.saveProfileState(complete)
    }

    override suspend fun setDarkMode(isDarkMode: Boolean) {
        return appStateDataStore.setDarkMode(isDarkMode)
    }

    override suspend fun setLanguageCode(languageCode: String) {
        return appStateDataStore.setLanguageCode(languageCode)
    }

    override fun getLogin(): Flow<Boolean> {
        return appStateDataStore.getLoginState()
    }

    override fun getRegister(): Flow<Boolean> {
        return appStateDataStore.getRegisterState()
    }

    override fun getBoarding(): Flow<Boolean> {
        return appStateDataStore.getBoardingState()
    }

    override fun getProfile(): Flow<Boolean> {
        return appStateDataStore.getProfileState()
    }

    override fun getDarkMode(): Flow<Boolean> {
        return appStateDataStore.getDarkMode()
    }

    override fun getLanguageCode(): Flow<String> {
        return appStateDataStore.getLanguageCode()
    }

    override suspend fun clearAppState() {
        return appStateDataStore.clearAppState()
    }

}
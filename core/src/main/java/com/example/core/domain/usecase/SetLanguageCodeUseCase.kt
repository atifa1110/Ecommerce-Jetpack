package com.example.core.domain.usecase

import com.example.core.data.local.datastore.AppStateDataStore
import javax.inject.Inject

class SetLanguageCodeUseCase @Inject constructor(
    private val appStateDataStore: AppStateDataStore
) {
    suspend operator fun invoke(languageCode: String)  {
        return appStateDataStore.setLanguageCode(languageCode)
    }
}
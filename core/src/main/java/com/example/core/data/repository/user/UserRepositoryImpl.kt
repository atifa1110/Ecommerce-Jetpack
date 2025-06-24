package com.example.core.data.repository.user

import com.example.core.domain.repository.user.UserRepository
import com.example.core.data.local.datastore.UserDataStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataStore: UserDataStore
) : UserRepository {
    override fun getUserName(): Flow<String> {
        return userDataStore.getUserName()
    }

    override fun getUserImage(): Flow<String> {
        return userDataStore.getUserImage()
    }

    override suspend fun clearUserData() {
        return userDataStore.clearUserData()
    }
}
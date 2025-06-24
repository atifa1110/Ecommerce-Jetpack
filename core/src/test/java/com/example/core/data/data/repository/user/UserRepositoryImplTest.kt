package com.example.core.data.data.repository.user

import com.example.core.data.local.datastore.UserDataStore
import com.example.core.data.repository.user.UserRepositoryImpl
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class UserRepositoryImplTest {

    private lateinit var userDataStore: UserDataStore
    private lateinit var repository: UserRepositoryImpl

    @Before
    fun setUp() {
        userDataStore = mockk(relaxed = true)
        repository = UserRepositoryImpl(userDataStore)
    }

    @Test
    fun `getUserName returns user name from data store`() = runTest {
        val expectedName = "John Doe"
        every { userDataStore.getUserName() } returns flowOf(expectedName)

        val result = repository.getUserName().first()
        assertEquals(expectedName, result)
    }

    @Test
    fun `getUserImage returns user image from data store`() = runTest {
        val expectedImage = "https://example.com/avatar.jpg"
        every { userDataStore.getUserImage() } returns flowOf(expectedImage)

        val result = repository.getUserImage().first()
        assertEquals(expectedImage, result)
    }

    @Test
    fun `clearUserData calls clearUserData on data store`() = runTest {
        repository.clearUserData()
        coVerify { userDataStore.clearUserData() }
    }
}

package com.example.core.data.data.local.datastore

import junit.framework.TestCase.assertEquals
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import com.example.core.data.local.datastore.AuthTokenDataStore
import com.example.core.data.local.datastore.UserDataStore
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class UserDataStoreTest {

    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var userDataStore: UserDataStore
    private lateinit var prefsFlow: MutableStateFlow<Preferences>

    @Before
    fun setup() {
        dataStore = mockk(relaxed = true)
        prefsFlow = MutableStateFlow(emptyPreferences())

        coEvery { dataStore.updateData(any()) } coAnswers {
            val block = it.invocation.args[0] as suspend (Preferences) -> Preferences
            val updated = block(prefsFlow.value)
            prefsFlow.value = updated
            updated
        }

        every { dataStore.data } returns prefsFlow

        userDataStore = UserDataStore(dataStore)
    }

    @Test
    fun `save and get username`() = runBlocking {
        val expectedName = "John Doe"
        userDataStore.saveUserName(expectedName)

        val actual = userDataStore.getUserName().first()
        assertEquals(expectedName, actual)
    }

    @Test
    fun `save and get user image`() = runBlocking {
        val expectedUrl = "https://example.com/image.jpg"
        userDataStore.saveUserImage(expectedUrl)

        val actual = userDataStore.getUserImage().first()
        assertEquals(expectedUrl, actual)
    }

    @Test
    fun `clear user data`() = runBlocking {
        userDataStore.saveUserName("Temp User")
        userDataStore.saveUserImage("https://image.com/temp.jpg")

        userDataStore.clearUserData()

        val name = userDataStore.getUserName().first()
        val image = userDataStore.getUserImage().first()

        assertEquals("", name)
        assertEquals("", image)
    }
}

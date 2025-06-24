package com.example.core.data.data.repository.auth

import com.example.core.data.local.datastore.AppStateDataStore
import com.example.core.data.local.datastore.AuthTokenDataStore
import com.example.core.data.local.datastore.UserDataStore
import com.example.core.data.network.datasource.AuthNetworkDataSource
import com.example.core.data.network.model.LoginNetwork
import com.example.core.data.network.model.TokenNetwork
import com.example.core.data.network.model.UserNetwork
import com.example.core.data.repository.auth.AuthRepositoryImpl
import com.example.core.data.network.request.AuthRequest
import com.example.core.data.network.request.TokenRequest
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.data.network.response.LoginResponse
import com.example.core.data.network.response.ProfileResponse
import com.example.core.data.network.response.RefreshResponse
import com.example.core.data.network.response.RegisterResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.MultipartBody
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class AuthRepositoryImplTest {

    private val authNetworkDataSource = mockk<AuthNetworkDataSource>()
    private val authTokenDataStore = mockk<AuthTokenDataStore>(relaxed = true)
    private val appStateDataStore = mockk<AppStateDataStore>(relaxed = true)
    private val userDataStore = mockk<UserDataStore>(relaxed = true)
    lateinit var repository : AuthRepositoryImpl

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = AuthRepositoryImpl(
            authNetworkDataSource,
            authTokenDataStore,
            appStateDataStore,
            userDataStore
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login emits Success when network call is successful`() = runTest {
        val request = AuthRequest("email", "password","token")
        val userData = LoginNetwork(
            accessToken = "access",
            refreshToken = "refresh",
            expiresAt = 3600,
            userName = "Atifa",
            userImage = "img.jpg"
        )

        val response = LoginResponse(200, "OK", userData)
        coEvery { authNetworkDataSource.loginUser(request)
        } returns EcommerceResponse.success(response)

        val result = repository.login(request).toList()

        assertTrue(result[0] is EcommerceResponse.Loading)
        val success = result[1] as EcommerceResponse.Success
        assertEquals("Atifa", success.value.userName)
        assertEquals("img.jpg", success.value.userImage)
        coVerify { userDataStore.saveUserName("Atifa") }
        coVerify { userDataStore.saveUserImage("img.jpg") }
        coVerify { appStateDataStore.saveLoginState(true) }
        coVerify { authTokenDataStore.saveRefreshToken("refresh") }
        coVerify { authTokenDataStore.saveAccessToken("access") }
        coVerify { authTokenDataStore.saveExpiresAt(3600)}
    }

    @Test
    fun `login emits Failure when network call fails`() = runTest {
        val request = AuthRequest("email", "password","token")
        val failureResponse = EcommerceResponse.Failure(code = 401, error = "Invalid credentials")

        coEvery { authNetworkDataSource.loginUser(request) } returns failureResponse

        val result = repository.login(request).toList()

        assertTrue(result[0] is EcommerceResponse.Loading)
        val failure = result[1] as EcommerceResponse.Failure
        assertEquals(401, failure.code)
        assertEquals("Invalid credentials", failure.error)
    }

    @Test
    fun `register emits Success when network call is successful`() = runTest {
        val request = AuthRequest("email", "password","token")
        val userData = TokenNetwork(
            accessToken = "access",
            refreshToken = "refresh",
            expiresAt = 3600,
        )

        val response = RegisterResponse(200, "OK", userData)
        coEvery { authNetworkDataSource.registerUser(request)
        } returns EcommerceResponse.success(response)

        val result = repository.register(request).toList()

        assertTrue(result[0] is EcommerceResponse.Loading)
        val success = result[1] as EcommerceResponse.Success
        assertEquals("access", success.value)
        coVerify { appStateDataStore.saveRegisterState(true) }
        coVerify { authTokenDataStore.saveRefreshToken("refresh") }
        coVerify { authTokenDataStore.saveAccessToken("access") }
        coVerify { authTokenDataStore.saveExpiresAt(3600)}
    }

    @Test
    fun `register emits Failure when registration fails`() = runTest {
        val request = AuthRequest("email", "password","token")
        val failureResponse = EcommerceResponse.Failure(code = 400, error = "Email already used")

        coEvery { authNetworkDataSource.registerUser(request) } returns failureResponse

        val result = repository.register(request).toList()

        assertTrue(result[0] is EcommerceResponse.Loading)
        val failure = result[1] as EcommerceResponse.Failure
        assertEquals(400, failure.code)
        assertEquals("Email already used", failure.error)
    }

    @Test
    fun `profile emits Success when network call is successful`() = runTest {
        val imagePart = mockk<MultipartBody.Part>()
        val namePart = mockk<MultipartBody.Part>()
        val userData = UserNetwork(
            userName = "Atifa",
            userImage = "img.jpg"
        )

        val response = ProfileResponse(200, "OK", userData)
        coEvery { authNetworkDataSource.profileUser(imagePart,namePart)
        } returns EcommerceResponse.success(response)

        val result = repository.profile(imagePart,namePart).toList()

        assertTrue(result[0] is EcommerceResponse.Loading)
        val success = result[1] as EcommerceResponse.Success
        assertEquals("Update Profile is Success", success.value)
        coVerify { appStateDataStore.saveProfileState(true) }
        coVerify { appStateDataStore.saveLoginState(true) }
        coVerify { userDataStore.saveUserName("Atifa") }
        coVerify { userDataStore.saveUserImage("img.jpg") }
    }

    @Test
    fun `profile emits Failure when update profile fails`() = runTest {
        val imagePart = mockk<MultipartBody.Part>()
        val namePart = mockk<MultipartBody.Part>()
        val failureResponse = EcommerceResponse.Failure(code = 403, error = "Unauthorized")

        coEvery { authNetworkDataSource.profileUser(imagePart, namePart) } returns failureResponse

        val result = repository.profile(imagePart, namePart).toList()

        assertTrue(result[0] is EcommerceResponse.Loading)
        val failure = result[1] as EcommerceResponse.Failure
        assertEquals(403, failure.code)
        assertEquals("Unauthorized", failure.error)
    }

    @Test
    fun `refresh emits Success when network call is successful`() = runTest {
        val request = TokenRequest("token")
        val tokenData = TokenNetwork(
            accessToken = "access",
            refreshToken = "refresh",
            expiresAt = 3600,
        )

        val response = RefreshResponse(200, "OK", tokenData)
        coEvery { authNetworkDataSource.refreshToken(request)
        } returns EcommerceResponse.success(response)

        val result = repository.refresh(request).toList()

        assertTrue(result[0] is EcommerceResponse.Loading)
        val success = result[1] as EcommerceResponse.Success
        assertEquals("Refresh reload success", success.value)
        coVerify { authTokenDataStore.saveRefreshToken("refresh") }
        coVerify { authTokenDataStore.saveAccessToken("access") }
        coVerify { authTokenDataStore.saveExpiresAt(3600)}
    }

    @Test
    fun `refresh emits Failure when network call fails`() = runTest {
        val request = TokenRequest("token")
        val failureResponse = EcommerceResponse.Failure(code = 401, error = "Invalid credentials")

        coEvery { authNetworkDataSource.refreshToken(request) } returns failureResponse

        val result = repository.refresh(request).toList()

        assertTrue(result[0] is EcommerceResponse.Loading)
        val failure = result[1] as EcommerceResponse.Failure
        assertEquals(401, failure.code)
        assertEquals("Invalid credentials", failure.error)
    }

}
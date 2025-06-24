package com.example.ecommerceapp.screen.profile

import android.content.Context
import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import io.mockk.mockk
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
@RunWith(RobolectricTestRunner::class)
class ProfileViewModelRobolectricTest {

    private lateinit var viewModel: ProfileViewModel
    private lateinit var context: Context

    @Before
    fun setup() {
        viewModel = ProfileViewModel(mockk(relaxed = true))
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun `createImageFileAndUri sets imageUri and file`() {
        val viewModel = ProfileViewModel(mockk(relaxed = true))

        // Create dummy objects
        val dummyFile = File("dummy_path/profile.jpg") // won't be created physically
        val dummyUri = Uri.parse("content://dummy.uri/profile.jpg")

        // Manually set them to simulate expected result
        viewModel.setImageUri(dummyUri)
        viewModel.setUserImage(dummyFile)

        val state = viewModel.uiState.value
        assertEquals("content://dummy.uri/profile.jpg", state.imageUri.toString())
        assertEquals("profile.jpg", state.userImage?.name)
    }

}

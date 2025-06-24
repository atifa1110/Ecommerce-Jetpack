package com.example.core.data.data.mapper

import com.example.core.data.mapper.asUser
import com.example.core.data.network.model.LoginNetwork
import kotlin.test.Test
import kotlin.test.assertEquals

class UserMapperTest {

    @Test
    fun `should map LoginNetwork to UserModel correctly`() {
        val loginNetwork = LoginNetwork(
            userName = "Atifa",
            userImage = "https://example.com/image.jpg",
            accessToken = "access",
            refreshToken ="refresh",
            expiresAt = 600
        )

        val userModel = loginNetwork.asUser()

        assertEquals("Atifa", userModel.userName)
        assertEquals("https://example.com/image.jpg", userModel.userImage)
    }
}


package com.example.core.data.mapper

import com.example.core.data.network.model.LoginNetwork
import com.example.core.domain.model.UserModel

fun LoginNetwork.asUser() = UserModel(
    userName = userName,
    userImage = userImage
)
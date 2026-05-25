package com.dorianbanic.core.data.mappers

import com.dorianbanic.core.data.dto.AuthInfoSerializable
import com.dorianbanic.core.data.dto.UserSerializable
import com.dorianbanic.core.domain.auth.AuthInfo
import com.dorianbanic.core.domain.auth.User

fun AuthInfoSerializable.toAuthInfo(): AuthInfo{
    return AuthInfo(
        accessToken = accessToken,
        refreshToken = refreshToken,
        user = user.toUser()
    )
}

fun AuthInfo.toAuthInfoSerializable(): AuthInfoSerializable{
    return AuthInfoSerializable(
        accessToken = accessToken,
        refreshToken = refreshToken,
        user = user.toSerializableUser()
    )
}

fun UserSerializable.toUser(): User{
    return User(
        id = id,
        email = email,
        username = username,
        hasVerifiedEmail = hasVerifiedEmail,
        profilePictureUrl = profilePictureUrl
    )
}

fun User.toSerializableUser(): UserSerializable{
    return UserSerializable(
        id = id,
        email = email,
        username = username,
        hasVerifiedEmail = hasVerifiedEmail,
        profilePictureUrl = profilePictureUrl
    )
}
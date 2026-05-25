package com.dorianbanic.chat.data.mappers

import com.dorianbanic.chat.data.dto.response.ProfilePictureUploadUrlsResponse
import com.dorianbanic.chat.domain.models.ProfilePictureUploadUrls

fun ProfilePictureUploadUrlsResponse.toDomain(): ProfilePictureUploadUrls {
    return ProfilePictureUploadUrls(
        uploadUrl = uploadUrl,
        publicUrl = publicUrl,
        headers = headers
    )
}
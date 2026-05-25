package com.dorianbanic.chat.domain.notification

interface CurrentDeviceTokenStore {
    suspend fun get(): String?
    suspend fun set(token: String?)
}
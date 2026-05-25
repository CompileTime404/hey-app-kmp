package com.dorianbanic.chat.data.notification

import com.dorianbanic.chat.domain.notification.CurrentDeviceTokenStore
import com.dorianbanic.chat.domain.notification.DeviceTokenService
import com.dorianbanic.core.domain.auth.SessionStorage
import com.google.firebase.messaging.FirebaseMessagingService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class HeyAppFirebaseMessagingService: FirebaseMessagingService() {

    private val deviceTokenService by inject<DeviceTokenService>()
    private val sessionStorage by inject<SessionStorage>()
    private val applicationScope by inject<CoroutineScope>()
    private val currentDeviceTokenStore by inject<CurrentDeviceTokenStore>()

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        applicationScope.launch {
            currentDeviceTokenStore.set(token)

            val authInfo = sessionStorage.observeAuthInfo().first()
            if (authInfo != null) {
                deviceTokenService.registerToken(
                    token = token,
                    platform = "ANDROID"
                )
            }
        }
    }
}
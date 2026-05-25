package com.dorianbanic.chat.data.notification

import com.dorianbanic.chat.domain.notification.PushNotificationService
import com.dorianbanic.core.domain.logging.HeyappLogger
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.coroutineContext

actual class FirebasePushNotificationService(
    private val logger: HeyappLogger
) :
    PushNotificationService {
    actual override fun observeDeviceToken(): Flow<String?> = flow {
        try {
            val fcmToken = FirebaseMessaging.getInstance().token.await()
            logger.info("Initial token is received: $fcmToken")
            emit(fcmToken)
        } catch (e: Exception) {
            coroutineContext.ensureActive()
            logger.error("Failed to get token", e)
            emit(null)
        }
    }
}
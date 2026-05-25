package com.dorianbanic.chat.data.di

import com.dorianbanic.chat.data.lifecycle.AppLifecycleObserver
import com.dorianbanic.chat.data.network.ConnectionErrorHandler
import com.dorianbanic.chat.data.network.ConnectivityObserver
import com.dorianbanic.chat.data.notification.FirebasePushNotificationService
import com.dorianbanic.chat.database.DatabaseFactory
import com.dorianbanic.chat.domain.notification.PushNotificationService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.binds
import org.koin.dsl.module

actual val platformChatDataModule = module {
    single { DatabaseFactory() }
    singleOf(::AppLifecycleObserver)
    singleOf(::ConnectivityObserver)
    singleOf(::ConnectionErrorHandler)
    singleOf(::FirebasePushNotificationService) binds PushNotificationService::class
}
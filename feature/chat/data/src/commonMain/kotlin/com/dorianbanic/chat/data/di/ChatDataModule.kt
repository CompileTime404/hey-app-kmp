package com.dorianbanic.chat.data.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.dorianbanic.chat.data.participant.KtorChatParticipantService
import com.dorianbanic.chat.data.chat.KtorChatService
import com.dorianbanic.chat.data.chat.OfflineFirstChatRepository
import com.dorianbanic.chat.data.chat.WebSocketChatConnectionClient
import com.dorianbanic.chat.data.message.KtorChatMessageService
import com.dorianbanic.chat.data.message.OfflineFirstMessageRepository
import com.dorianbanic.chat.data.network.ConnectionErrorHandler
import com.dorianbanic.chat.data.network.ConnectionRetryHandler
import com.dorianbanic.chat.data.network.KtorWebSocketConnector
import com.dorianbanic.chat.data.notification.DataStoreCurrentDeviceTokenStore
import com.dorianbanic.chat.data.notification.KtorDeviceTokenService
import com.dorianbanic.chat.data.participant.OfflineFirstChatParticipantRepository
import com.dorianbanic.chat.database.DatabaseFactory
import com.dorianbanic.chat.domain.chat.ChatConnectionClient
import com.dorianbanic.chat.domain.participant.ChatParticipantService
import com.dorianbanic.chat.domain.chat.ChatRepository
import com.dorianbanic.chat.domain.chat.ChatService
import com.dorianbanic.chat.domain.message.ChatMessageService
import com.dorianbanic.chat.domain.message.MessageRepository
import com.dorianbanic.chat.domain.notification.CurrentDeviceTokenStore
import com.dorianbanic.chat.domain.notification.DeviceTokenService
import com.dorianbanic.chat.domain.participant.ChatParticipantRepository
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformChatDataModule: Module

val chatDataModule = module {
    includes(platformChatDataModule)
    singleOf(::KtorChatParticipantService) bind ChatParticipantService::class
    singleOf(::KtorChatService) bind ChatService::class
    singleOf(::OfflineFirstChatRepository) bind ChatRepository::class
    singleOf(::OfflineFirstMessageRepository) bind MessageRepository::class
    singleOf(::WebSocketChatConnectionClient) bind ChatConnectionClient::class
    singleOf(::ConnectionRetryHandler)
    singleOf(::KtorWebSocketConnector)
    singleOf(::KtorChatMessageService) bind ChatMessageService::class
    singleOf(::OfflineFirstChatParticipantRepository) bind ChatParticipantRepository::class
    singleOf(::KtorDeviceTokenService) bind DeviceTokenService::class
    singleOf(::DataStoreCurrentDeviceTokenStore) bind CurrentDeviceTokenStore::class
    single {
        Json {
            ignoreUnknownKeys = true
        }
    }
    single {
        get<DatabaseFactory>()
            .create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
}
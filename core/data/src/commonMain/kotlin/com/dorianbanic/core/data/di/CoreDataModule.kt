package com.dorianbanic.core.data.di

import com.dorianbanic.core.data.auth.DataStoreSessionStorage
import com.dorianbanic.core.data.auth.KtorAuthService
import com.dorianbanic.core.data.logging.KermitLogger
import com.dorianbanic.core.data.networking.HttpClientFactory
import com.dorianbanic.core.domain.auth.AuthService
import com.dorianbanic.core.domain.auth.SessionStorage
import com.dorianbanic.core.domain.logging.HeyappLogger
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformCoreDataModule: Module

val coreDataModule = module {
    includes(platformCoreDataModule)
    single<HeyappLogger> { KermitLogger }
    single {
        HttpClientFactory(get(), get()).create(get())
    }
    singleOf(::KtorAuthService) bind AuthService::class
    singleOf(::DataStoreSessionStorage) bind SessionStorage::class
}
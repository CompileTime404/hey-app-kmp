package com.dorianbanic.heyapp.di

import com.dorianbanic.auth.presentation.di.authPresentationModule
import com.dorianbanic.chat.data.di.chatDataModule
import com.dorianbanic.chat.presentation.di.chatPresentationModule
import com.dorianbanic.core.data.di.coreDataModule
import com.dorianbanic.core.presentation.di.corePresentationModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            coreDataModule,
            authPresentationModule,
            appModule,
            chatPresentationModule,
            corePresentationModule,
            chatDataModule
        )
    }
}
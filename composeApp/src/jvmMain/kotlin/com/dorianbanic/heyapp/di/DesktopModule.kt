package com.dorianbanic.heyapp.di

import com.dorianbanic.heyapp.ApplicationStateHolder
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val desktopModule = module {
    singleOf(::ApplicationStateHolder)
}
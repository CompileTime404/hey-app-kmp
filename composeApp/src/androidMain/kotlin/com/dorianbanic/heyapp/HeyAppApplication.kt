package com.dorianbanic.heyapp

import android.app.Application
import com.dorianbanic.heyapp.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class HeyAppApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@HeyAppApplication)
            androidLogger()
        }
    }
}
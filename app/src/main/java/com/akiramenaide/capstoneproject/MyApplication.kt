package com.akiramenaide.capstoneproject

import android.app.Application
import com.akiramenaide.capstoneproject.ui.di.useCaseModule
import com.akiramenaide.capstoneproject.ui.di.viewModelModule
import com.akiramenaide.core.di.databaseModule
import com.akiramenaide.core.di.networkModule
import com.akiramenaide.core.di.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApplication)
            modules(
                listOf(
                    databaseModule,
                    networkModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule
                )
            )
        }
    }
}
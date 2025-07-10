package dev.balikin.poject.di

import dev.balikin.poject.storage.DatabaseFactory
import dev.balikin.poject.storage.createDataStore
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single { DatabaseFactory(androidApplication()) }
        single { createDataStore(context = androidContext()) }
        single { OkHttp.create() }
    }
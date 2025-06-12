package dev.balikin.poject.di

import dev.balikin.poject.storage.DatabaseFactory
import dev.icerock.moko.permissions.PermissionsController
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single { DatabaseFactory(androidApplication()) }
    }
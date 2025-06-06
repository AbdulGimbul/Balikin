package dev.balikin.poject.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            provideOnBoardingRepositoryModule,
            provideAuthRepositoryModule,
            provideTransactionRepositoryModule,
            provideHomeRepositoryModule,
            provideLocalStorageModule,
            provideHistoryRepositoryModule,
            platformModule
        )
    }
}
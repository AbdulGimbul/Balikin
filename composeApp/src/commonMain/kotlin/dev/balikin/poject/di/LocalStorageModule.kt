package dev.balikin.poject.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import dev.balikin.poject.storage.BalikinDatabase
import dev.balikin.poject.storage.DatabaseFactory
import dev.balikin.poject.storage.SessionHandler
import org.koin.dsl.module

val provideLocalStorageModule = module {
    single { SessionHandler(get()) }
    single {
        get<DatabaseFactory>().create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single { get<BalikinDatabase>().transactionDao }
    single { get<BalikinDatabase>().historyDao }
}

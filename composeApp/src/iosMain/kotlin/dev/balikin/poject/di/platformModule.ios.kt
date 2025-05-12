package dev.balikin.poject.di

import dev.balikin.poject.storage.BalikinDatabase
import dev.balikin.poject.storage.getBalikinDatabase
import dev.balikin.poject.storage.getDatabaseBuilder
import org.koin.core.module.Module
import org.koin.dsl.module


actual fun platformModule(): Module = module {
    single<BalikinDatabase> {
        val builder = getDatabaseBuilder()
        getBalikinDatabase(builder)
    }
}
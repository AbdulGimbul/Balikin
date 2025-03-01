package dev.balikin.poject

import android.app.Application
import dev.balikin.poject.di.initKoin
import org.koin.android.ext.koin.androidContext

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@MyApplication)
        }
    }
}
package dev.balikin.poject

import android.app.Application
import android.content.Context
import dev.balikin.poject.di.initKoin
import org.koin.android.ext.koin.androidContext

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@MyApplication)
        }
        appContext = applicationContext

        multiplatform.network.cmptoast.AppContext.apply { set(appContext) }
    }

    companion object {
        lateinit var appContext: Context
            private set
    }
}
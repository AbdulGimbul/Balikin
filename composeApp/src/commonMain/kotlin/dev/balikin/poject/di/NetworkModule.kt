package dev.balikin.poject.di

import dev.balikin.poject.network.BalikinHttpClientBuilder
import dev.balikin.poject.network.RequestHandler
import io.ktor.http.URLProtocol
import org.koin.dsl.module

val provideHttpClientModule = module {
    single {
        BalikinHttpClientBuilder(get())
            .protocol(URLProtocol.HTTPS)
            .host("balikin.vercel.app")
            .build(get())
    }

    single { RequestHandler(get()) }
}
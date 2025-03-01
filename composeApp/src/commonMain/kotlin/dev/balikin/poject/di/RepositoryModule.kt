package dev.balikin.poject.di

import dev.balikin.poject.features.auth.presentation.login.LoginViewModel
import dev.balikin.poject.features.auth.presentation.register.RegisterViewModel
import dev.balikin.poject.features.front_page.data.OnBoardingRepository
import dev.balikin.poject.features.front_page.data.OnBoardingRepositoryImpl
import dev.balikin.poject.features.front_page.presentation.OnBoardingViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val provideOnBoardingRepositoryModule = module {
    single<OnBoardingRepositoryImpl> {
        OnBoardingRepositoryImpl()
    }.bind<OnBoardingRepository>()
    viewModel { OnBoardingViewModel(repository = get()) }
}

val provideAuthRepositoryModule = module {
    viewModel { RegisterViewModel() }
    viewModel { LoginViewModel() }
}
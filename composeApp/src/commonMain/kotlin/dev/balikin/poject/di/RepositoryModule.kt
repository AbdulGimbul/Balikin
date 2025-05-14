package dev.balikin.poject.di

import dev.balikin.poject.features.auth.presentation.login.LoginViewModel
import dev.balikin.poject.features.auth.presentation.register.RegisterViewModel
import dev.balikin.poject.features.front_page.data.OnBoardingRepository
import dev.balikin.poject.features.front_page.data.OnBoardingRepositoryImpl
import dev.balikin.poject.features.front_page.presentation.OnBoardingViewModel
import dev.balikin.poject.features.history.data.HistoryRepository
import dev.balikin.poject.features.history.data.HistoryRepositoryImpl
import dev.balikin.poject.features.history.presentation.HistoryViewModel
import dev.balikin.poject.features.home.presentation.HomeViewModel
import dev.balikin.poject.features.transaction.data.TransactionRepository
import dev.balikin.poject.features.transaction.data.TransactionRepositoryImpl
import dev.balikin.poject.features.transaction.presentation.TransactionViewModel
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

val provideTransactionRepositoryModule = module {
    single<TransactionRepositoryImpl> {
        TransactionRepositoryImpl(transactionDao = get())
    }.bind<TransactionRepository>()
    viewModel { TransactionViewModel(transactionRepository = get()) }
}

val provideHomeRepositoryModule = module {
    viewModel { HomeViewModel(transactionRepository = get()) }
}

val provideHistoryRepositoryModule = module {
    single<HistoryRepositoryImpl> {
        HistoryRepositoryImpl(historyDao = get())
    }.bind<HistoryRepository>()
    viewModel {
        HistoryViewModel(historyRepository = get())
    }
}
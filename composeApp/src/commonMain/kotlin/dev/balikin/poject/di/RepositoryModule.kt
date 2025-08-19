package dev.balikin.poject.di

import dev.balikin.poject.features.auth.data.AuthRepository
import dev.balikin.poject.features.auth.data.AuthRepositoryImpl
import dev.balikin.poject.features.auth.presentation.login.LoginViewModel
import dev.balikin.poject.features.auth.presentation.profile.ProfileViewModel
import dev.balikin.poject.features.auth.presentation.register.RegisterViewModel
import dev.balikin.poject.features.friends.data.FriendsRepository
import dev.balikin.poject.features.friends.data.FriendsRepositoryImpl
import dev.balikin.poject.features.friends.presentation.FriendsViewModel
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
    viewModel { OnBoardingViewModel(repository = get(), sessionHandler = get()) }
}

val provideAuthRepositoryModule = module {
    single<AuthRepositoryImpl> {
        AuthRepositoryImpl(sessionHandler = get(), requestHandler = get())
    }.bind<AuthRepository>()
    viewModel { RegisterViewModel() }
    viewModel { LoginViewModel(get(), get(), get()) }
    viewModel { ProfileViewModel(get(), get()) }
}

val provideTransactionRepositoryModule = module {
    single<TransactionRepositoryImpl> {
        TransactionRepositoryImpl(requestHandler = get(),transactionDao = get())
    }.bind<TransactionRepository>()
    viewModel { TransactionViewModel(transactionRepository = get()) }
}

val provideHomeRepositoryModule = module {
    viewModel { HomeViewModel(authRepository = get(), transactionRepository = get(), friendsRepository = get()) }
}

val provideHistoryRepositoryModule = module {
    single<HistoryRepositoryImpl> {
        HistoryRepositoryImpl(historyDao = get())
    }.bind<HistoryRepository>()
    viewModel {
        HistoryViewModel(historyRepository = get())
    }
}

val provideFriendsRepositoryModule = module {
    single<FriendsRepositoryImpl> {
        FriendsRepositoryImpl(requestHandler = get())
    }.bind<FriendsRepository>()
    viewModel { FriendsViewModel(friendsRepository = get()) }
}
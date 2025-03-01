package dev.balikin.poject.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object OnBoarding : Screen("onboarding")
}
package dev.balikin.poject.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object OnBoarding : Screen("onboarding")
    data object ForgotPassword : Screen("forgot_password")
    data object ResetPassword : Screen("reset_password")
    data object SetNewPassword : Screen("set_new_password")
    data object Transaction : Screen("transaction")
    data object FilterTrans : Screen("filter_trans")
    data object FilterHistory : Screen("filter_history")
    data object History : Screen("history")
    data object Profile : Screen("profile")
}
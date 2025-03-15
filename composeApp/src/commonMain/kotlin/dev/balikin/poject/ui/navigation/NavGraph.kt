package dev.balikin.poject.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import dev.balikin.poject.features.auth.presentation.login.LoginScreen
import dev.balikin.poject.features.auth.presentation.login.LoginViewModel
import dev.balikin.poject.features.auth.presentation.register.RegisterScreen
import dev.balikin.poject.features.auth.presentation.register.RegisterViewModel
import dev.balikin.poject.features.auth.presentation.forgot_password.ForgotPasswordScreen
import dev.balikin.poject.features.auth.presentation.forgot_password.ForgotPasswordViewModel
import dev.balikin.poject.features.auth.presentation.reset_password.ResetPasswordScreen
import dev.balikin.poject.features.auth.presentation.reset_password.ResetPasswordViewModel
import dev.balikin.poject.features.auth.presentation.set_password.SetNewPasswordScreen
import dev.balikin.poject.features.auth.presentation.set_password.SetNewPasswordViewModel
import dev.balikin.poject.features.front_page.presentation.OnBoardingScreen
import dev.balikin.poject.features.front_page.presentation.OnBoardingViewModel
import dev.balikin.poject.features.home.presentation.HomeScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SetupNavHost(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
//                bottomBar = {
//                    if (currentRoute in listOf(
//                            Screen.Home.route,
//                            Screen.Sales.route,
//                            Screen.Product.route,
//                            Screen.Profile.route,
//                        )
//                    ) {
//                        BottomBar(navController)
//                    }
//                },
    ) { innerPadding ->
        NavHostContent(
            navController = navController,
            innerPadding = innerPadding,
        )
    }
}

@Composable
fun NavHostContent(
    navController: NavHostController,
    innerPadding: PaddingValues,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = koinViewModel<LoginViewModel>(),
                navController = navController
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                viewModel = koinViewModel<RegisterViewModel>(),
                navController = navController
            )
        }
        composable(Screen.OnBoarding.route) {
            OnBoardingScreen(
                viewModel = koinViewModel<OnBoardingViewModel>(),
                navController = navController
            )
        }
        composable(Screen.Home.route) {
            HomeScreen()
        }
        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                viewModel = ForgotPasswordViewModel(),
                navController = navController
            )
        }
        composable(Screen.ResetPassword.route) {
            ResetPasswordScreen(
                viewModel = ResetPasswordViewModel(),
                navController = navController
            )
        }
        composable(Screen.SetNewPassword.route) {
            SetNewPasswordScreen(
                viewModel = SetNewPasswordViewModel(),
                navController = navController
            )
        }
    }
}

//@Composable
//private fun BottomBar(
//    navController: NavHostController,
//    modifier: Modifier = Modifier
//) {
//    NavigationBar(
//        modifier = modifier
//    ) {
//        val navBackStackEntry by navController.currentBackStackEntryAsState()
//        val currentRoute = navBackStackEntry?.destination?.route
//
//        navigationItems.map { item ->
//            NavigationBarItem(
//                selected = currentRoute == item.screen.route,
//                onClick = {
//                    navController.navigate(item.screen.route) {
//                        navController.graph.startDestinationRoute?.let {
//                            popUpTo(Screen.Home.route) {
//                                saveState = true
//                            }
//                            restoreState = true
//                            launchSingleTop = true
//                        }
//                    }
//                },
//                icon = {
//                    Icon(
//                        imageVector = item.icon,
//                        contentDescription = item.title
//                    )
//                },
//                label = {
//                    Text(text = item.title)
//                },
//                colors = NavigationBarItemDefaults.colors(
//                    selectedIconColor = primary,
//                    selectedTextColor = primary,
//                    indicatorColor = Color.Transparent
//                )
//            )
//        }
//    }
//}

//val navigationItems = listOf(
//    BottomRailNavItem(
//        title = "Beranda",
//        icon = Icons.Outlined.Home,
//        screen = Screen.Home
//    ),
//    BottomRailNavItem(
//        title = "Penjualan",
//        icon = Icons.Outlined.ShoppingCart,
//        screen = Screen.Sales
//    ),
//    BottomRailNavItem(
//        title = "Barang",
//        icon = Icons.Outlined.Domain,
//        screen = Screen.Product
//    ),
//    BottomRailNavItem(
//        title = "Akun",
//        icon = Icons.Outlined.AccountCircle,
//        screen = Screen.Profile
//    )
//)
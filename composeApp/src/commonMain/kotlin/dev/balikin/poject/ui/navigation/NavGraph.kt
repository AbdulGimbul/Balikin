package dev.balikin.poject.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import dev.balikin.poject.features.auth.presentation.forgot_password.ForgotPasswordScreen
import dev.balikin.poject.features.auth.presentation.forgot_password.ForgotPasswordViewModel
import dev.balikin.poject.features.auth.presentation.login.LoginScreen
import dev.balikin.poject.features.auth.presentation.login.LoginViewModel
import dev.balikin.poject.features.auth.presentation.profile.ProfileScreen
import dev.balikin.poject.features.auth.presentation.register.RegisterScreen
import dev.balikin.poject.features.auth.presentation.register.RegisterViewModel
import dev.balikin.poject.features.auth.presentation.reset_password.ResetPasswordScreen
import dev.balikin.poject.features.auth.presentation.reset_password.ResetPasswordViewModel
import dev.balikin.poject.features.auth.presentation.set_password.SetNewPasswordScreen
import dev.balikin.poject.features.auth.presentation.set_password.SetNewPasswordViewModel
import dev.balikin.poject.features.front_page.presentation.OnBoardingScreen
import dev.balikin.poject.features.front_page.presentation.OnBoardingViewModel
import dev.balikin.poject.features.home.presentation.HomeScreen
import dev.balikin.poject.features.home.presentation.HomeViewModel
import dev.balikin.poject.features.transaction.presentation.TransactionScreen
import dev.balikin.poject.features.transaction.presentation.TransactionViewModel
import dev.balikin.poject.ui.components.DefaultButton
import dev.balikin.poject.ui.components.FilterScreen
import dev.balikin.poject.utils.formatDate
import dev.balikin.poject.utils.formattedDate
import dev.balikin.poject.utils.getCurrentDate
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupNavHost(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var showBottomSheet by remember { mutableStateOf(false) }
    val homeViewModel: HomeViewModel = koinViewModel()

    Scaffold(
        bottomBar = {
            if (currentRoute in listOf(
                    Screen.Home.route,
                    Screen.Transaction.route,
                    Screen.History.route,
                    Screen.Profile.route,
                )
            ) {
                BottomBarWithFab(navController, onFabClick = { showBottomSheet = true })
            }
        },
    ) { innerPadding ->
        Box {
            NavHostContent(
                navController = navController,
                innerPadding = innerPadding,
                homeViewModel = homeViewModel
            )
            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showBottomSheet = false },
                    sheetState = sheetState,
                ) {
                    AddTransactionBottomSheet(
                        viewModel = homeViewModel,
                        onSaveClicked = {
                            showBottomSheet = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun NavHostContent(
    navController: NavHostController,
    innerPadding: PaddingValues,
    homeViewModel: HomeViewModel
) {
    val transactionViewModel: TransactionViewModel = koinViewModel()

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
            HomeScreen(viewModel = homeViewModel)
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
        composable(Screen.Transaction.route) {
            TransactionScreen(viewModel = transactionViewModel, navController = navController)
        }
        composable(Screen.FilterTrans.route) {
            FilterScreen(
                viewModel = transactionViewModel,
                navController = navController
            )
        }
        composable(Screen.Profile.route) {
            ProfileScreen()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddTransactionBottomSheet(
    viewModel: HomeViewModel,
    onSaveClicked: () -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    val transactionTypes = listOf("Utang", "Piutang")
    var expanded by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf("Utang") }
    var amount by remember { mutableStateOf("") }
    var date by remember { mutableStateOf<LocalDateTime>(getCurrentDate()) }
    var note by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            "Tambah Transaksi Utang Piutang",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        )
        HorizontalDivider(modifier = Modifier.padding(top = 16.dp))
        Column(
            modifier = Modifier
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { RequiredLabel("Nama") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = selectedType,
                        onValueChange = { /* no-op: changes come from menu selection */ },
                        label = { RequiredLabel("Jenis Transaksi") },
                        modifier = Modifier
                            .menuAnchor(type = MenuAnchorType.PrimaryEditable),
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        transactionTypes.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type) },
                                onClick = {
                                    selectedType = type
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = amount,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() }) {
                            amount = newValue
                        }
                    },
                    label = { RequiredLabel("Amount") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    )
                )

            }

            OutlinedTextField(
                value = formatDate(date),
                onValueChange = {  },
                readOnly = true,
                label = { RequiredLabel("Tanggal") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(
                            imageVector = Icons.Outlined.CalendarMonth,
                            contentDescription = "Select date"
                        )
                    }
                },
                placeholder = { Text("dd/mm/yyyy") }
            )

            if (showDatePicker) {
                DatePickerModal(
                    onDateSelected = { millis ->
                        val localDate = millis?.let { Instant.fromEpochMilliseconds(it) }
                            ?.toLocalDateTime(TimeZone.currentSystemDefault())?.date
                        val currentTime =
                            Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time

                        val combined = localDate?.let { LocalDateTime(it, currentTime) }

                        if (combined != null) {
                            date = combined
                        }
                    },
                    onDismiss = { showDatePicker = false }
                )
            }

            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Keterangan") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                maxLines = 3
            )

            DefaultButton(
                onClick = {
                    viewModel.addTransaction(
                        name = name,
                        date = date.toString(),
                        note = note,
                        amount = amount,
                        type = selectedType
                    )
                    onSaveClicked()
                },
                modifier = Modifier.fillMaxWidth(),
                text = "Simpan"
            )
        }
    }
}

@Composable
fun RequiredLabel(label: String) {
    Text(
        buildAnnotatedString {
            append(label)
            append(" ")
            withStyle(style = SpanStyle(color = Color.Red)) {
                append("*")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}


//val navigationItems = listOf(
//    BottomNavItem(
//        title = "Home",
//        icon = Icons.Outlined.Home,
//        screen = Screen.Home
//    ),
//    BottomNavItem(
//        title = "Transaksi",
//        icon = Icons.Outlined.Payments,
//        screen = Screen.Transaction
//    ),
//    BottomNavItem(
//        title = "History",
//        icon = Icons.Outlined.History,
//        screen = Screen.History
//    ),
//    BottomNavItem(
//        title = "Profile",
//        icon = Icons.Outlined.AccountCircle,
//        screen = Screen.Profile
//    )
//)
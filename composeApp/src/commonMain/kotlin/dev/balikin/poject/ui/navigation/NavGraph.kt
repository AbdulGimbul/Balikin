package dev.balikin.poject.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import balikin.composeapp.generated.resources.Res
import balikin.composeapp.generated.resources.ic_calendar
import balikin.composeapp.generated.resources.offline_profile
import dev.balikin.poject.features.auth.presentation.forgot_password.ForgotPasswordScreen
import dev.balikin.poject.features.auth.presentation.forgot_password.ForgotPasswordViewModel
import dev.balikin.poject.features.auth.presentation.login.LoginScreen
import dev.balikin.poject.features.auth.presentation.login.LoginViewModel
import dev.balikin.poject.features.auth.presentation.register.RegisterScreen
import dev.balikin.poject.features.auth.presentation.register.RegisterViewModel
import dev.balikin.poject.features.auth.presentation.reset_password.ResetPasswordScreen
import dev.balikin.poject.features.auth.presentation.reset_password.ResetPasswordViewModel
import dev.balikin.poject.features.auth.presentation.set_password.SetNewPasswordScreen
import dev.balikin.poject.features.auth.presentation.set_password.SetNewPasswordViewModel
import dev.balikin.poject.features.front_page.presentation.OnBoardingScreen
import dev.balikin.poject.features.front_page.presentation.OnBoardingViewModel
import dev.balikin.poject.features.history.presentation.HistoryScreen
import dev.balikin.poject.features.history.presentation.HistoryViewModel
import dev.balikin.poject.features.history.presentation.filter.HistoryFilterScreen
import dev.balikin.poject.features.home.presentation.HomeScreen
import dev.balikin.poject.features.home.presentation.HomeViewModel
import dev.balikin.poject.features.transaction.presentation.TransactionScreen
import dev.balikin.poject.features.transaction.presentation.TransactionViewModel
import dev.balikin.poject.features.transaction.presentation.filter.TransFilterScreen
import dev.balikin.poject.ui.components.DefaultButton
import dev.balikin.poject.ui.theme.primary_blue
import dev.balikin.poject.ui.theme.primary_text
import dev.balikin.poject.ui.theme.red
import dev.balikin.poject.ui.theme.secondary_text
import dev.balikin.poject.ui.theme.stroke
import dev.balikin.poject.utils.ThousandSeparatorVisualTransformation
import dev.balikin.poject.utils.formatDate
import dev.balikin.poject.utils.getCurrentDate
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import multiplatform.network.cmptoast.showToast
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SetupNavHost(navController: NavHostController, onExitApp: () -> Unit) {
    var backPressedOnce by remember { mutableStateOf(false) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var showBottomSheet by remember { mutableStateOf(false) }
    val homeViewModel: HomeViewModel = koinViewModel()

    if (currentRoute == Screen.Home.route) {
        BackHandler {
            if (backPressedOnce) {
                onExitApp()
            } else {
                backPressedOnce = true
                showToast("Press back again to exit")
            }
        }

        LaunchedEffect(Unit) {
            delay(2000)
            backPressedOnce = false
        }
    }

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
    val historyViewModel: HistoryViewModel = koinViewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.OnBoarding.route,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(Screen.Login.route) {
            LoginScreen(viewModel = koinViewModel<LoginViewModel>(), navController = navController)
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
            ResetPasswordScreen(viewModel = ResetPasswordViewModel(), navController = navController)
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
            TransFilterScreen(viewModel = transactionViewModel, navController = navController)
        }
        composable(Screen.Profile.route) {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(Res.drawable.offline_profile),
                    contentDescription = null,
                    modifier = Modifier.size(200.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                val info = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = primary_text
                        )
                    ) {
                        append("Terimakasih telah menunggu!")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.SemiBold,
                            color = secondary_text
                        )
                    ) {
                        append(" Fitur ini masih dalam tahap pengembangan. kami akan segera melakukan update secepat mungkin.")
                    }
                }
                Text(
                    text = info,
                    textAlign = TextAlign.Center
                )
            }
        }
        composable(Screen.History.route) {
            HistoryScreen(viewModel = historyViewModel, navController = navController)
        }
        composable(Screen.FilterHistory.route) {
            HistoryFilterScreen(viewModel = historyViewModel, navController = navController)
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
    var amount by remember { mutableStateOf(TextFieldValue("")) }
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
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = stroke,
                    focusedBorderColor = primary_blue
                )
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
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                            unfocusedBorderColor = stroke,
                            focusedBorderColor = primary_blue
                        )
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
                    value = amount.text,
                    onValueChange = { newValue ->
                        val cleaned = newValue.filter { it.isDigit() }
                        amount =
                            TextFieldValue(text = cleaned, selection = TextRange(cleaned.length))
                    },
                    label = { RequiredLabel("Amount") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.NumberPassword
                    ),
                    visualTransformation = ThousandSeparatorVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = stroke,
                        focusedBorderColor = primary_blue
                    )
                )
            }

            OutlinedTextField(
                value = formatDate(date),
                onValueChange = { },
                readOnly = true,
                label = { RequiredLabel("Tanggal") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_calendar),
                            contentDescription = "Select date",
                            tint = primary_blue,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                placeholder = { Text("dd/mm/yyyy") },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = stroke,
                    focusedBorderColor = primary_blue
                )
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
                maxLines = 3,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = stroke,
                    focusedBorderColor = primary_blue
                )
            )

            DefaultButton(
                onClick = {
                    val rawAmount = amount.text
                    if (rawAmount.isNotBlank() && rawAmount.toDoubleOrNull() != 0.0 && name.isNotBlank()) {
                        viewModel.addTransaction(
                            name = name,
                            date = date.toString(),
                            note = note,
                            amount = rawAmount,
                            type = selectedType
                        )
                        onSaveClicked()
                    } else {
                        showToast(
                            message = "Data yang anda isi belum lengkap!",
                            backgroundColor = red
                        )
                    }
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
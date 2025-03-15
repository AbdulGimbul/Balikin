package dev.balikin.poject.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Payments
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
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
import dev.balikin.poject.features.home.presentation.HomeScreen
import dev.balikin.poject.ui.components.DefaultButton
import dev.balikin.poject.ui.theme.grey
import dev.balikin.poject.ui.theme.primary_blue
import dev.balikin.poject.utils.formattedDate
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupNavHost(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

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
            )
            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showBottomSheet = false },
                    sheetState = sheetState,
                ) {
                    AddTransactionBottomSheet(
                        onSaveClicked = { showBottomSheet = false }
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

@Composable
private fun BottomBarWithFab(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onFabClick: () -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(modifier = modifier.height(80.dp)) {
        HorizontalDivider(color = grey, thickness = 0.5.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left side items
            navigationItems.take(2).forEach { item ->
                BottomBarItem(
                    item = item,
                    selected = currentRoute == item.screen.route,
                    onClick = { navController.navigate(item.screen.route) }
                )
            }

            // Right side spacer for FAB area
            Spacer(modifier = Modifier.weight(1f))

            // Right side items
            navigationItems.drop(2).forEach { item ->
                BottomBarItem(
                    item = item,
                    selected = currentRoute == item.screen.route,
                    onClick = { navController.navigate(item.screen.route) }
                )
            }
        }

        // Centered FAB
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = 6.dp)
                .size(56.dp)
                .background(
                    color = primary_blue,
                    shape = CircleShape
                )
                .clickable { onFabClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun BottomBarItem(
    item: BottomNavItem,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(80.dp)
            .clickable(onClick = onClick)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.title,
            tint = if (selected) primary_blue else Color.Gray
        )
        Text(
            text = item.title,
            color = if (selected) primary_blue else Color.Gray,
            fontSize = 12.sp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddTransactionBottomSheet(onSaveClicked: () -> Unit = {}) {
    var name by remember { mutableStateOf("Irfan Nur I") }
    val transactionTypes = listOf("Utang", "Piutang")
    var expanded by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf("Utang") }
    var amount by remember { mutableStateOf("450.000") }
    var date by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("Bekas beli burung") }
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
                    onValueChange = { amount = it },
                    label = { RequiredLabel("Amount") },
                    modifier = Modifier.weight(1f)
                )

            }

            OutlinedTextField(
                value = date,
                onValueChange = { date = it },
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
                    onDateSelected = { date = formattedDate(it ?: 0) },
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
                onClick = onSaveClicked,
                modifier = Modifier.fillMaxWidth(),
                text = "Simpan"
            )
        }
    }
}

@Composable
fun RequiredLabel(label: String) {
    // Use an AnnotatedString to color only the asterisk
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


val navigationItems = listOf(
    BottomNavItem(
        title = "Home",
        icon = Icons.Outlined.Home,
        screen = Screen.Home
    ),
    BottomNavItem(
        title = "Transaksi",
        icon = Icons.Outlined.Payments,
        screen = Screen.Transaction
    ),
    BottomNavItem(
        title = "History",
        icon = Icons.Outlined.History,
        screen = Screen.History
    ),
    BottomNavItem(
        title = "Profile",
        icon = Icons.Outlined.AccountCircle,
        screen = Screen.Profile
    )
)
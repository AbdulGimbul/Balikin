package dev.balikin.poject.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tweener.alarmee.AlarmeeService
import com.tweener.alarmee.model.Alarmee
import com.tweener.alarmee.model.AndroidNotificationConfiguration
import com.tweener.alarmee.model.IosNotificationConfiguration
import dev.balikin.poject.features.auth.data.AuthRepository
import dev.balikin.poject.features.friends.data.FriendsRepository
import dev.balikin.poject.features.friends.domain.FollowedUser
import dev.balikin.poject.features.transaction.data.TransactionEntity
import dev.balikin.poject.features.transaction.data.TransactionRepository
import dev.balikin.poject.features.transaction.data.TransactionType
import dev.balikin.poject.features.transaction.domain.CreateOnlineTransactionRequest
import dev.balikin.poject.features.transaction.domain.UnifiedTransaction
import dev.balikin.poject.network.onError
import dev.balikin.poject.network.onSuccess
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.notifications.REMOTE_NOTIFICATION
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class HomeViewModel(
    private val authRepository: AuthRepository,
    private val transactionRepository: TransactionRepository,
    private val friendsRepository: FriendsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.userInfo().collect { user ->
                _uiState.update { it.copy(user = user, isLoading = false) }
                if (user != null) {
                    getLatestTransactions()
                    loadUnifiedTransactions()
                }
            }
        }
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.OnToggleSelected -> {
                _uiState.value = _uiState.value.copy(selectedTab = event.type)
            }

            is HomeUiEvent.LoadTotalAmountByType -> {
                getTotalAmountByType(event.type)
            }

            HomeUiEvent.LoadLatestTransactions -> {
                getLatestTransactions()
            }

            is HomeUiEvent.SearchFriends -> {
                searchFriends(event.keyword)
            }

            is HomeUiEvent.SelectFriend -> {
                _uiState.update { it.copy(selectedFriend = event.friend) }
            }

            HomeUiEvent.ClearFriendSuggestions -> {
                _uiState.update { it.copy(friendSuggestions = emptyList()) }
            }
        }
    }

    fun getLatestTransactions() {
        viewModelScope.launch {
            transactionRepository.getAllTransactions()
                .collect { transactions ->
                    _uiState.value = _uiState.value.copy(latestTransactions = transactions)
                }
        }
    }

    fun getTotalAmountByType(type: TransactionType) {
        viewModelScope.launch {
            val totalAmount = transactionRepository.getTotalAmountByType(type) ?: 0.0
            _uiState.value = _uiState.value.copy(totalAmount = totalAmount)
        }
    }

    @OptIn(ExperimentalTime::class)
    fun addTransaction(
        name: String,
        date: String,
        note: String,
        amount: String,
        type: String,
        permissionsController: PermissionsController,
        alarmeeService: AlarmeeService,
    ) {
        _uiState.update { it.copy(permissionError = null) }

        viewModelScope.launch {

            val transactionType = when (type.lowercase()) {
                "utang" -> TransactionType.Utang
                "piutang" -> TransactionType.Piutang
                else -> TransactionType.Utang
            }

            val dueDate: LocalDateTime = try {
                LocalDateTime.parse(date)
            } catch (e: Exception) {
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            }

            val transaction = TransactionEntity(
                name = name,
                dueDate = dueDate,
                note = note,
                amount = amount.toDouble(),
                type = transactionType
            )
            val newTransactionId = transactionRepository.addTransaction(transaction)

            try {
                permissionsController.providePermission(Permission.REMOTE_NOTIFICATION)

                alarmeeService.local.schedule(
                    alarmee = Alarmee(
                        uuid = newTransactionId.toString(),
                        notificationTitle = "Reminder: $transactionType Jatuh Tempo",
                        notificationBody = "$transactionType Anda kepada $name akan jatuh tempo hari ini.",
                        scheduledDateTime = dueDate,
                        androidNotificationConfiguration = AndroidNotificationConfiguration(
                            channelId = "due_date_reminders"
                        ),
                        iosNotificationConfiguration = IosNotificationConfiguration()
                    )
                )

            } catch (deniedAlways: DeniedAlwaysException) {
                _uiState.update { it.copy(permissionError = "Izin notifikasi ditolak. Pengingat tidak akan aktif. Anda bisa mengaktifkannya di pengaturan aplikasi.") }
            } catch (denied: DeniedException) {
                _uiState.update { it.copy(permissionError = "Izin notifikasi ditolak. Pengingat tidak akan aktif.") }
            }

            val currentUiType = when (_uiState.value.selectedTab.lowercase()) {
                "utang" -> TransactionType.Utang
                "piutang" -> TransactionType.Piutang
                else -> TransactionType.Piutang
            }

            getTotalAmountByType(currentUiType)
        }
    }

    fun searchFriends(keyword: String) {
        if (keyword.isBlank()) {
            _uiState.update { it.copy(friendSuggestions = emptyList()) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingFriends = true) }

            val result = friendsRepository.searchFollowing(
                keyword = keyword,
                limit = "10",
                offset = "0"
            )

            result.onSuccess { followingResponse ->
                val friends = followingResponse.data.map { it.followed }
                _uiState.update {
                    it.copy(
                        friendSuggestions = friends,
                        isLoadingFriends = false
                    )
                }
            }.onError {
                _uiState.update {
                    it.copy(
                        friendSuggestions = emptyList(),
                        isLoadingFriends = false
                    )
                }
            }
        }
    }

    fun addTransactionWithFriendSupport(
        name: String,
        date: String,
        note: String,
        amount: String,
        type: String,
        selectedFriend: FollowedUser?,
        permissionsController: PermissionsController,
        alarmeeService: AlarmeeService,
    ) {

        _uiState.update {
            it.copy(
                permissionError = null,
                onlineTransactionError = null,
                isCreatingOnlineTransaction = false
            )
        }

        viewModelScope.launch {
            // Check if user is logged in and has selected a friend
            val currentUser = _uiState.value.user
            val shouldCreateOnline = currentUser != null && selectedFriend != null

            if (shouldCreateOnline) {
                // Create online transaction
                _uiState.update { it.copy(isCreatingOnlineTransaction = true) }

                val kategori = when (type.uppercase()) {
                    "UTANG" -> "UTANG"
                    "PIUTANG" -> "PIUTANG"
                    else -> "PIUTANG"
                }

                val request = CreateOnlineTransactionRequest(
                    email = selectedFriend!!.email,
                    nominal = amount,
                    kategori = kategori,
                    desc = note,
                    date = date
                )

                val result = transactionRepository.createOnlineTransaction(request)

                result.onSuccess { response ->
                    _uiState.update {
                        it.copy(
                            isCreatingOnlineTransaction = false,
                            onlineTransactionError = null
                        )
                    }

                    // Refresh latest transactions after successful online creation
                    getLatestTransactions()
                    loadUnifiedTransactions() // Also refresh unified transactions

                    val currentUiType = when (_uiState.value.selectedTab.lowercase()) {
                        "utang" -> TransactionType.Utang
                        "piutang" -> TransactionType.Piutang
                        else -> TransactionType.Piutang
                    }
                    getTotalAmountByType(currentUiType)

                }.onError { error ->
                    _uiState.update {
                        it.copy(
                            isCreatingOnlineTransaction = false,
                            onlineTransactionError = "Failed to create online transaction: ${error.message}"
                        )
                    }

                    // Fallback to local storage if online fails
                    createLocalTransaction(
                        name,
                        date,
                        note,
                        amount,
                        type,
                        permissionsController,
                        alarmeeService
                    )
                }
            } else {
                // Create local transaction (existing logic)
                createLocalTransaction(
                    name,
                    date,
                    note,
                    amount,
                    type,
                    permissionsController,
                    alarmeeService
                )
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    private suspend fun createLocalTransaction(
        name: String,
        date: String,
        note: String,
        amount: String,
        type: String,
        permissionsController: PermissionsController,
        alarmeeService: AlarmeeService,
    ) {
        val transactionType = when (type.lowercase()) {
            "utang" -> TransactionType.Utang
            "piutang" -> TransactionType.Piutang
            else -> TransactionType.Utang
        }

        val dueDate: LocalDateTime = try {
            LocalDateTime.parse(date)
        } catch (e: Exception) {
            Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        }

        val transaction = TransactionEntity(
            name = name,
            dueDate = dueDate,
            note = note,
            amount = amount.toDouble(),
            type = transactionType
        )
        val newTransactionId = transactionRepository.addTransaction(transaction)

        try {

            // Check current permission status first
            val isProvided =
                permissionsController.isPermissionGranted(Permission.REMOTE_NOTIFICATION)

            if (!isProvided) {
                permissionsController.providePermission(Permission.REMOTE_NOTIFICATION)
            } else {
                println("🔥 Permission already granted, skipping request")
            }

            alarmeeService.local.schedule(
                alarmee = Alarmee(
                    uuid = newTransactionId.toString(),
                    notificationTitle = "Reminder: $transactionType Jatuh Tempo",
                    notificationBody = "$transactionType Anda kepada $name akan jatuh tempo hari ini.",
                    scheduledDateTime = dueDate,
                    androidNotificationConfiguration = AndroidNotificationConfiguration(
                        channelId = "due_date_reminders"
                    ),
                    iosNotificationConfiguration = IosNotificationConfiguration()
                )
            )

        } catch (deniedAlways: DeniedAlwaysException) {
            _uiState.update { it.copy(permissionError = "Izin notifikasi ditolak. Pengingat tidak akan aktif. Anda bisa mengaktifkannya di pengaturan aplikasi.") }
        } catch (denied: DeniedException) {
            _uiState.update { it.copy(permissionError = "Izin notifikasi ditolak. Pengingat tidak akan aktif.") }
        } catch (e: Exception) {
            e.printStackTrace()
            _uiState.update { it.copy(permissionError = "Error requesting notification permission: ${e.message}") }
        }

        val currentUiType = when (_uiState.value.selectedTab.lowercase()) {
            "utang" -> TransactionType.Utang
            "piutang" -> TransactionType.Piutang
            else -> TransactionType.Piutang
        }

        getTotalAmountByType(currentUiType)
    }

    fun loadUnifiedTransactions() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoadingOnlineTransactions = true,
                    onlineTransactionListError = null
                )
            }

            // Load local transactions
            val localTransactionsResult = kotlin.runCatching {
                transactionRepository.getAllTransactions().first()
            }

            val localTransactions = localTransactionsResult.getOrElse { emptyList() }

            // Load online transactions if user is logged in
            val currentUser = _uiState.value.user
            if (currentUser != null) {
                val onlineResult = transactionRepository.getOnlineTransactions()

                onlineResult.onSuccess { response ->
                    // Convert local transactions to unified
                    val localUnified = localTransactions.map {
                        UnifiedTransaction.fromLocal(it, currentUser.email)
                    }

                    // Convert online transactions to unified
                    val onlineUnified = response.data.map {
                        UnifiedTransaction.fromOnline(it, currentUser.email)
                    }

                    // Combine and sort by date (newest first)
                    val allTransactions = (localUnified + onlineUnified)
                        .sortedByDescending { it.date }

                    _uiState.update {
                        it.copy(
                            unifiedTransactions = allTransactions,
                            isLoadingOnlineTransactions = false,
                            onlineTransactionListError = null
                        )
                    }

                }.onError { error ->
                    // If online fails, still show local transactions
                    val localUnified = localTransactions.map {
                        UnifiedTransaction.fromLocal(it, currentUser.email)
                    }

                    _uiState.update {
                        it.copy(
                            unifiedTransactions = localUnified,
                            isLoadingOnlineTransactions = false,
                            onlineTransactionListError = "Failed to load online transactions: ${error.message}"
                        )
                    }
                }
            } else {
                // User not logged in, show only local transactions
                val localUnified = localTransactions.map {
                    UnifiedTransaction.fromLocal(it)
                }

                _uiState.update {
                    it.copy(
                        unifiedTransactions = localUnified,
                        isLoadingOnlineTransactions = false,
                        onlineTransactionListError = null
                    )
                }
            }
        }
    }
}

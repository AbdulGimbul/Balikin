package dev.balikin.poject.features.home.presentation

import dev.balikin.poject.features.auth.domain.UserData
import dev.balikin.poject.features.friends.domain.FollowedUser
import dev.balikin.poject.features.transaction.data.TransactionEntity
import dev.balikin.poject.features.transaction.domain.UnifiedTransaction

data class HomeUiState(
    val totalAmount: Double = 0.0,
    val latestTransactions: List<TransactionEntity> = emptyList(),
    val selectedTab: String = "Utang",
    val permissionError: String? = null,
    val isLoading: Boolean = false,
    val user: UserData? = null,
    
    // Friend suggestions for add transaction
    val friendSuggestions: List<FollowedUser> = emptyList(),
    val isLoadingFriends: Boolean = false,
    val selectedFriend: FollowedUser? = null,
    
    // Online transaction state
    val isCreatingOnlineTransaction: Boolean = false,
    val onlineTransactionError: String? = null,
    
    // Unified transactions (both local and online)
    val unifiedTransactions: List<UnifiedTransaction> = emptyList(),
    val isLoadingOnlineTransactions: Boolean = false,
    val onlineTransactionListError: String? = null
)

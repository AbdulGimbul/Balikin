package dev.balikin.poject.features.home.presentation

import dev.balikin.poject.features.friends.domain.FollowedUser
import dev.balikin.poject.features.transaction.data.TransactionType

sealed class HomeUiEvent {
    data class OnToggleSelected(val type: String) : HomeUiEvent()
    data class LoadTotalAmountByType(val type: TransactionType) : HomeUiEvent()
    object LoadLatestTransactions : HomeUiEvent()
    
    // Friend suggestion events for add transaction
    data class SearchFriends(val keyword: String) : HomeUiEvent()
    data class SelectFriend(val friend: FollowedUser?) : HomeUiEvent()
    object ClearFriendSuggestions : HomeUiEvent()
}

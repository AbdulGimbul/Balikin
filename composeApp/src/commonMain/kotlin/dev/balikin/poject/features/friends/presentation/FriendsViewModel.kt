package dev.balikin.poject.features.friends.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.balikin.poject.features.friends.data.FriendsRepository
import dev.balikin.poject.network.onError
import dev.balikin.poject.network.onSuccess
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FriendsViewModel(
    private val friendsRepository: FriendsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FriendsUiState())
    val uiState = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        searchFriends("")
    }

    fun onEvent(event: FriendsUiEvent) {
        when (event) {
            is FriendsUiEvent.OnQueryChanged -> {
                _uiState.value = _uiState.value.copy(nameSearch = event.query)
                searchFriends(event.query)
            }
        }
    }

    private fun searchFriends(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300)

            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            val result = friendsRepository.searchFriends(
                keyword = query,
                limit = "10",
                offset = "0"
            )

            result.onSuccess {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    friends = it.data
                )
            }.onError {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = it.message
                )
            }
        }
    }
}

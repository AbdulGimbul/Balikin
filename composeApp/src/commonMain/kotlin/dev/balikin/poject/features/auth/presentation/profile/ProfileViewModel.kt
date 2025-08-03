package dev.balikin.poject.features.auth.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.balikin.poject.features.auth.data.AuthRepository
import dev.balikin.poject.features.friends.data.FriendsRepository
import dev.balikin.poject.network.onError
import dev.balikin.poject.network.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(
    private val authRepository: AuthRepository,
    private val friendsRepository: FriendsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        if (_uiState.value.userData == null) {
            getUserData()
        }
        loadFriends()
    }

    fun onEvent(event: ProfileUiEvent) {
        when (event) {
            is ProfileUiEvent.Logout -> logout()
        }
    }

    private fun getUserData() {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                authRepository.userInfo().collect { user ->
                    _uiState.update { it.copy(userData = user, isLoading = false) }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)
                e.printStackTrace()
            }
        }
    }

    private fun logout() {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch(Dispatchers.IO) {

            val result = authRepository.logout()
            withContext(Dispatchers.Main) {
                result.onSuccess { _uiState.update { it.copy(isLogout = true) } }
                    .onFailure { e -> _uiState.update { it.copy(errorMessage = e.message) } }
            }
        }
    }
    
    private fun loadFriends() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingFriends = true)
            
            val result = friendsRepository.getFollowing()
            
            result.onSuccess { followingResponse ->
                val friends = followingResponse.data.map { it.followed }
                _uiState.value = _uiState.value.copy(
                    isLoadingFriends = false,
                    friends = friends
                )
            }.onError {
                _uiState.value = _uiState.value.copy(
                    isLoadingFriends = false
                )
            }
        }
    }
}

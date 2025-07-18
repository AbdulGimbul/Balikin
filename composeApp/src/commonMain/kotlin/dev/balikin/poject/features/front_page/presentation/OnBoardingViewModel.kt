package dev.balikin.poject.features.front_page.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.balikin.poject.features.front_page.data.OnBoardingRepository
import dev.balikin.poject.storage.SessionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OnBoardingViewModel(
    private val repository: OnBoardingRepository,
    private val sessionHandler: SessionHandler
) : ViewModel() {
    private val _uiState = MutableStateFlow(OnBoardingUiState())
    val uiState: StateFlow<OnBoardingUiState> = _uiState

    val pages = repository.getOnboardingDatas()

    init {
        _uiState.update { it.copy(pages = pages) }
    }

    fun onEvent(event: OnBoardingUiEvent) {
        when (event) {
            is OnBoardingUiEvent.PageChanged -> updatePage(event.newPage)
        }
    }

    fun onCompleteOnboarding() {
        viewModelScope.launch {
            sessionHandler.setOnboardingCompleted()
        }
    }

    private fun updatePage(newPage: Int) {
        _uiState.update {
            it.copy(
                currentPage = newPage,
                isLastPage = newPage == pages.lastIndex
            )
        }
    }
}
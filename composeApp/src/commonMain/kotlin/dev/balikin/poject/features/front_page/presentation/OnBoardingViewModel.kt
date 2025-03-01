package dev.balikin.poject.features.front_page.presentation

import androidx.lifecycle.ViewModel
import dev.balikin.poject.features.front_page.data.OnBoardingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class OnBoardingViewModel(
    private val repository: OnBoardingRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(OnBoardingUiState())
    val uiState: StateFlow<OnBoardingUiState> = _uiState

    val pages = repository.getOnboardingDatas()

    init {
        _uiState.update { it.copy(pages = pages) }
    }

    fun onEvent(event: OnBoardingUiEvent) {
        when (event) {
            OnBoardingUiEvent.SignInClicked -> handleSignIn()
            is OnBoardingUiEvent.PageChanged -> updatePage(event.newPage)
        }
    }

    private fun handleNext() {
        val nextPage = _uiState.value.currentPage + 1
        if (nextPage < pages.size) {
            updatePage(nextPage)
        }
    }

    private fun handleSkip() {
        updatePage(pages.lastIndex)
    }

    private fun updatePage(newPage: Int) {
        _uiState.update {
            it.copy(
                currentPage = newPage,
                isLastPage = newPage == pages.lastIndex
            )
        }
    }

    private fun handleSignIn() {
        // Handle sign in logic
    }
}
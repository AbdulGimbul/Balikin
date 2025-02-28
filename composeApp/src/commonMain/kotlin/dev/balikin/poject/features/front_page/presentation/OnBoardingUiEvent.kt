package dev.balikin.poject.features.front_page.presentation

sealed class OnBoardingUiEvent {
    object SignInClicked : OnBoardingUiEvent()
    data class PageChanged(val newPage: Int) : OnBoardingUiEvent()
}
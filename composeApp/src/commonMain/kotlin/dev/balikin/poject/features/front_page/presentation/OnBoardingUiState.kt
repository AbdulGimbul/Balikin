package dev.balikin.poject.features.front_page.presentation

import dev.balikin.poject.features.front_page.domain.OnBoarding

data class OnBoardingUiState(
    val currentPage: Int = 0,
    val isLastPage: Boolean = false,
    val pages: List<OnBoarding> = emptyList()
)
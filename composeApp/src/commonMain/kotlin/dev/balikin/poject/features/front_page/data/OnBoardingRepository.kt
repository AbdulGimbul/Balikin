package dev.balikin.poject.features.front_page.data

import dev.balikin.poject.features.front_page.domain.OnBoarding

interface OnBoardingRepository {
    fun getOnboardingDatas(): List<OnBoarding>
}
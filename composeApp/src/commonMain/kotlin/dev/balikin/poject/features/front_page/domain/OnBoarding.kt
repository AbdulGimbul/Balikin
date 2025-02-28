package dev.balikin.poject.features.front_page.domain

import androidx.compose.ui.text.AnnotatedString
import org.jetbrains.compose.resources.DrawableResource

data class OnBoarding(
    val imageRes: DrawableResource,
    val title: AnnotatedString,
    val description: String
)

package dev.balikin.poject.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import balikin.composeapp.generated.resources.Manrope_Bold
import balikin.composeapp.generated.resources.Manrope_ExtraBold
import balikin.composeapp.generated.resources.Manrope_ExtraLight
import balikin.composeapp.generated.resources.Manrope_Light
import balikin.composeapp.generated.resources.Manrope_Medium
import balikin.composeapp.generated.resources.Manrope_Regular
import balikin.composeapp.generated.resources.Manrope_SemiBold
import balikin.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.Font

@Composable
fun ManropeFontFamily() = FontFamily(
    Font(Res.font.Manrope_Regular, FontWeight.Normal),
    Font(Res.font.Manrope_Bold, FontWeight.Bold),
    Font(Res.font.Manrope_ExtraBold, FontWeight.ExtraBold),
    Font(Res.font.Manrope_ExtraLight, FontWeight.ExtraLight),
    Font(Res.font.Manrope_Light, FontWeight.Light),
    Font(Res.font.Manrope_Medium, FontWeight.Medium),
    Font(Res.font.Manrope_SemiBold, FontWeight.SemiBold),
)

@Composable
fun ManropeTypography() = Typography().run {

    val fontFamily = ManropeFontFamily()
    copy(
        displayLarge = displayLarge.copy(fontFamily = fontFamily),
        displayMedium = displayMedium.copy(fontFamily = fontFamily),
        displaySmall = displaySmall.copy(fontFamily = fontFamily),
        headlineLarge = headlineLarge.copy(fontFamily = fontFamily),
        headlineMedium = headlineMedium.copy(fontFamily = fontFamily),
        headlineSmall = headlineSmall.copy(fontFamily = fontFamily),
        titleLarge = titleLarge.copy(fontFamily = fontFamily),
        titleMedium = titleMedium.copy(fontFamily = fontFamily),
        titleSmall = titleSmall.copy(fontFamily = fontFamily),
        bodyLarge = bodyLarge.copy(fontFamily = fontFamily),
        bodyMedium = bodyMedium.copy(fontFamily = fontFamily),
        bodySmall = bodySmall.copy(fontFamily = fontFamily),
        labelLarge = labelLarge.copy(fontFamily = fontFamily),
        labelMedium = labelMedium.copy(fontFamily = fontFamily),
        labelSmall = labelSmall.copy(fontFamily = fontFamily)
    )
}

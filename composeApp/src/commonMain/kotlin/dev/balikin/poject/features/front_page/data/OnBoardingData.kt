package dev.balikin.poject.features.front_page.data

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import balikin.composeapp.generated.resources.Res
import balikin.composeapp.generated.resources.phone
import balikin.composeapp.generated.resources.phone_coins
import balikin.composeapp.generated.resources.phone_creditcard
import dev.balikin.poject.features.front_page.domain.OnBoarding

object OnBoardingData {
    val onBoardingDatas = listOf(
        OnBoarding(
            imageRes = Res.drawable.phone,
            title = buildAnnotatedString {
                append("Pantau Semua Transaksi, \nHindari")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(" Lupa Bayar!")
                }
            },
            description = "Catat setiap transaksi, dapatkan pengingat otomatis dan kendalikan keuangan utang piutang dengan mudah dan tanpa ribet."
        ),
        OnBoarding(
            imageRes = Res.drawable.phone_creditcard,
            title = buildAnnotatedString {
                append("Kelola Utang & Piutang")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(" dengan\n Mudah dan Aman.")
                }
            },
            description = "Jangan biarkan utang atau piutang tidak tercatat atau terkelola dengan baik."
        ),
        OnBoarding(
            imageRes = Res.drawable.phone_coins,
            title = buildAnnotatedString {
                append("Transaksi")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(" Jelas")
                }
                append(" dan Hubungan\n Tetap")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(" Lancar.")
                }
            },
            description = "Catat utang dan piutang dengan akurat untuk menghindari kesalahpahaman dan menjaga kepercayaan."
        )
    )
}
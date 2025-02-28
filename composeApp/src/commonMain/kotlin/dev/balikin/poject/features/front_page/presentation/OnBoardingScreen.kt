package dev.balikin.poject.features.front_page.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import balikin.composeapp.generated.resources.Res
import balikin.composeapp.generated.resources.balikin_logo
import balikin.composeapp.generated.resources.phone
import balikin.composeapp.generated.resources.phone_coins
import balikin.composeapp.generated.resources.phone_creditcard
import dev.balikin.poject.ui.theme.primary_blue
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

data class OnboardingPage(
    val imageRes: DrawableResource,
    val title: AnnotatedString,
    val description: String
)

@Composable
fun OnboardingScreen(onFinish: () -> Unit) {

    val pages = listOf(
        OnboardingPage(
            imageRes = Res.drawable.phone,
            title = buildAnnotatedString {
                append("Pantau Semua Transaksi, \nHindari")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(" Lupa Bayar!")
                }
            },
            description = "Catat setiap transaksi, dapatkan pengingat otomatis dan kendalikan keuangan utang piutang dengan mudah dan tanpa ribet."
        ),
        OnboardingPage(
            imageRes = Res.drawable.phone_creditcard,
            title = buildAnnotatedString {
                append("Kelola Utang & Piutang")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(" dengan\n Mudah dan Aman.")
                }
            },
            description = "Jangan biarkan utang atau piutang tidak tercatat atau terkelola dengan baik."
        ),
        OnboardingPage(
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

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                getBackgroundGradient(pagerState.currentPage)
            ).
            padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (page == 2) {
                    Image(
                        painter = painterResource(Res.drawable.balikin_logo),
                        contentDescription = null,
                        modifier = Modifier.size(100.dp).padding(bottom = 10.dp)
                    )
                    Image(
                        painter = painterResource(pages[page].imageRes),
                        contentDescription = null,
                        modifier = Modifier.size(250.dp)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = pages[page].title,
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = pages[page].description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                    )
                } else {
                    Text(
                        text = pages[page].title,
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Image(
                        painter = painterResource(pages[page].imageRes),
                        contentDescription = null,
                        modifier = Modifier.size(250.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = pages[page].description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .weight(0.15f),
            horizontalArrangement = Arrangement.Center,
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = when {
                    pagerState.currentPage == pagerState.pageCount - 1 && iteration == pagerState.pageCount - 1 -> primary_blue
                    pagerState.currentPage == iteration -> Color.White
                    else -> Color.White.copy(alpha = 0.4f)
                }
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(10.dp)
                        .background(color)
                )

                if (iteration < pagerState.pageCount - 1) {
                    Spacer(modifier = Modifier.width(6.dp))
                }
            }
        }

        Button(
            onClick = {
                if (pagerState.currentPage < pagerState.pageCount - 1) {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                } else {
                    onFinish()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = primary_blue
            ),
            shape = RoundedCornerShape(10.dp),
        ) {
            Text(
                text = if (pagerState.currentPage == pagerState.pageCount - 1) "Get Started" else "Next",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(vertical = 6.dp)
            )
        }

        if (pagerState.currentPage == pagerState.pageCount - 1) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Already have an account? ",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )

                Text(
                    text = "Sign in",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = primary_blue,
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                    }
                )
            }
        } else {
            Text(
                text = "Skip",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.pageCount - 1)
                        }
                    },
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }
    }
}

private fun getBackgroundGradient(currentPage: Int): Brush {
    return if (currentPage == 2) {
        Brush.linearGradient(listOf(Color(0xFF5865F2), Color(0xFF9C86FC), Color(0XFF5865F2)))
    } else {
        Brush.radialGradient(listOf(Color(0xFF838DFF), Color(0xFF5865F2)), radius = 1000f)
    }
}
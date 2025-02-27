package dev.balikin.poject.features.front_page.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import balikin.composeapp.generated.resources.Res
import balikin.composeapp.generated.resources.phone
import balikin.composeapp.generated.resources.phone_creditcard
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
            imageRes = Res.drawable.phone_creditcard,
            title = buildAnnotatedString {
                append("Pantau Semua Transaksi, \nHindari")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(" Lupa Bayar!")
                }
            },
            description = "Discover amazing features and benefits of our app."
        ),
        OnboardingPage(
            imageRes = Res.drawable.phone,
            title = buildAnnotatedString {
                append("Kelola Utang & Piutang")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(" dengan\n Mudah dan Aman.")
                }
            },
            description = "Browse through various sections and content."
        ),
        OnboardingPage(
            imageRes = Res.drawable.phone_creditcard,
            title = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.Blue)) {
                append("Balikin")
            }
            },
            description = "Join our community and start your journey."
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val coroutineScope = rememberCoroutineScope()

    // Define different gradients
    val defaultGradient = Brush.radialGradient(
        colors = listOf(Color(0xFF838DFF), Color(0xFF5865F2)),
        radius = 1000f
    )

    val lastPageGradient = Brush.radialGradient(
        colors = listOf(Color(0xFF4CAF50), Color(0xFF81C784), Color.White),
    )

    Column(
        modifier = Modifier.fillMaxSize()
            .background(
                brush = if (pagerState.currentPage == pages.lastIndex)
                    lastPageGradient
                else
                    defaultGradient
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = pages[page].title,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(32.dp))

                Image(
                    painter = painterResource(resource = pages[page].imageRes),
                    contentDescription = null,
                    modifier = Modifier.size(250.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = pages[page].description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
            }
        }

        // Page indicators
        Row(
            Modifier
                .height(16.dp)
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary
                    else Color.LightGray
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(12.dp)
                        .background(color)
                )
            }
        }

        // Next/Finish button
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
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Text(
                text = if (pagerState.currentPage == pagerState.pageCount - 1) "Get Started" else "Next"
            )
        }
    }
}
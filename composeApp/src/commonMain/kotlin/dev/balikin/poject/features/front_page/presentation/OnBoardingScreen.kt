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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import balikin.composeapp.generated.resources.Res
import balikin.composeapp.generated.resources.balikin_logo
import balikin.composeapp.generated.resources.bg_onboarding
import dev.balikin.poject.ui.theme.primary_blue
import dev.balikin.poject.ui.theme.primary_text
import dev.balikin.poject.ui.theme.secondary_text
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

@Composable
fun OnBoardingScreen(viewModel: OnBoardingViewModel) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    Onboarding(uiState = uiState, onEvent = viewModel::onEvent)
}

@Composable
fun Onboarding(uiState: OnBoardingUiState, onEvent: (OnBoardingUiEvent) -> Unit) {

    val pagerState = rememberPagerState(pageCount = { uiState.pages.size })
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(uiState.currentPage) {
        if (pagerState.currentPage != uiState.currentPage) {
            pagerState.animateScrollToPage(uiState.currentPage)
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage != uiState.currentPage) {
            onEvent(OnBoardingUiEvent.PageChanged(pagerState.currentPage))
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    getBackgroundGradient(uiState.currentPage)
                )
        )

        if (uiState.isLastPage) {
            Image(
                painter = painterResource(Res.drawable.bg_onboarding),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
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
                            painter = painterResource(uiState.pages[page].imageRes),
                            contentDescription = null,
                            modifier = Modifier.size(250.dp)
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        Text(
                            text = uiState.pages[page].title,
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Center,
                            color = primary_text
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = uiState.pages[page].description,
                            style = MaterialTheme.typography.bodyLarge,
                            color = secondary_text,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                    } else {
                        Text(
                            text = uiState.pages[page].title,
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        Image(
                            painter = painterResource(uiState.pages[page].imageRes),
                            contentDescription = null,
                            modifier = Modifier.size(250.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = uiState.pages[page].description,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 24.dp)
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
                        uiState.isLastPage && iteration == pagerState.pageCount - 1 -> primary_blue
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
//                        onFinish()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (uiState.isLastPage) primary_blue else Color.White,
                    contentColor = if (uiState.isLastPage) Color.White else primary_blue
                ),
                shape = RoundedCornerShape(10.dp),
            ) {
                Text(
                    text = if (uiState.isLastPage) "Get Started" else "Next",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(vertical = 6.dp)
                )
            }

            if (uiState.isLastPage) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Already have an account? ",
                        style = MaterialTheme.typography.titleMedium,
                        color = secondary_text
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
}

private fun getBackgroundGradient(currentPage: Int): Brush {
    return if (currentPage == 2) {
        Brush.linearGradient(listOf(Color(0xFFCECFE9), Color(0xFFE9E9EF), Color(0XFFD1D2E7)))
    } else {
        Brush.radialGradient(listOf(Color(0xFF838DFF), Color(0xFF5865F2)), radius = 1000f)
    }
}
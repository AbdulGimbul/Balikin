package dev.balikin.poject.features.front_page.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import balikin.composeapp.generated.resources.Res
import balikin.composeapp.generated.resources.balikin_logo
import balikin.composeapp.generated.resources.bg_onboarding
import dev.balikin.poject.ui.components.DefaultButton
import dev.balikin.poject.ui.navigation.Screen
import dev.balikin.poject.ui.theme.primary_blue
import dev.balikin.poject.ui.theme.primary_text
import dev.balikin.poject.ui.theme.secondary_text
import dev.balikin.poject.utils.getBrowserHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

@Composable
fun OnBoardingScreen(viewModel: OnBoardingViewModel, navController: NavController) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    Onboarding(
        uiState = uiState.value,
        onEvent = viewModel::onEvent,
        moveToLogin = {
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.OnBoarding.route) {
                    inclusive = true
                }
            }
        },
        moveToHome = {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.OnBoarding.route) {
                    inclusive = true
                }
            }
        }
    )
}

@Composable
fun Onboarding(
    uiState: OnBoardingUiState,
    onEvent: (OnBoardingUiEvent) -> Unit,
    moveToLogin: () -> Unit,
    moveToHome: () -> Unit
) {

    val pagerState = rememberPagerState(pageCount = { uiState.pages.size })
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .distinctUntilChanged()
            .collect { page ->
                onEvent(OnBoardingUiEvent.PageChanged(page))
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

        AnimatedVisibility(
            visible = uiState.isLastPage,
            enter = fadeIn()
        ) {
            Image(
                painter = painterResource(Res.drawable.bg_onboarding),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Column {
            Column(
                modifier = Modifier.weight(1f),
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

                DefaultButton(
                    text = if (uiState.isLastPage) "Get Started" else "Next",
                    onClick = {
                        if (pagerState.currentPage < pagerState.pageCount - 1) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        } else {
                            moveToHome()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (uiState.isLastPage) primary_blue else Color.White,
                        contentColor = if (uiState.isLastPage) Color.White else primary_blue
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, 16.dp),
                )

                if (uiState.isLastPage) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 32.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Already have an account? ",
                            style = MaterialTheme.typography.bodyLarge,
                            color = secondary_text
                        )

                        Text(
                            text = "Sign in",
                            style = MaterialTheme.typography.bodyLarge,
                            color = primary_blue,
                            modifier = Modifier.clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                moveToLogin()
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
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Icons by ",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.3f)
                    )
                    Text(
                        text = "icons8",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White.copy(alpha = 0.5f),
                            textDecoration = TextDecoration.Underline
                        ),
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            getBrowserHelper().openBrowser("https://icons8.com/")
                        }
                    )
                }
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
package dev.balikin.poject.features.home.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import balikin.composeapp.generated.resources.Res
import balikin.composeapp.generated.resources.agus
import balikin.composeapp.generated.resources.balikin_white
import balikin.composeapp.generated.resources.bg_card
import balikin.composeapp.generated.resources.chip
import balikin.composeapp.generated.resources.money_wings
import dev.balikin.poject.features.transaction.data.TransactionType
import dev.balikin.poject.ui.components.TransactionItem
import dev.balikin.poject.ui.theme.grey2
import dev.balikin.poject.ui.theme.primary_blue
import dev.balikin.poject.ui.theme.primary_text
import dev.balikin.poject.ui.theme.secondary_text
import dev.balikin.poject.utils.currencyFormat
import org.jetbrains.compose.resources.painterResource

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Home(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun Home(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit
) {

    LaunchedEffect(uiState.selectedTab) {
        val type = when (uiState.selectedTab.lowercase()) {
            "utang" -> TransactionType.Utang
            "piutang" -> TransactionType.Piutang
            else -> TransactionType.Utang
        }
        onEvent(HomeUiEvent.LoadTotalAmountByType(type))
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(Res.drawable.agus), contentDescription = null,
                modifier = Modifier.size(40.dp).padding(end = 8.dp),
            )
            Column {
                Text(
                    text = "Hello, Fatamore",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = primary_text
                    )
                )
                Text(
                    text = "Kosongkan utangmu yak! ;)",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = secondary_text
                    )
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = null,
                    tint = primary_text
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        DebtCard(
            totalDebt = currencyFormat(uiState.totalAmount),
            selectedTab = uiState.selectedTab,
            onTabSelected = { onEvent(HomeUiEvent.OnToggleSelected(it)) }
        )

        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier.background(
                color = primary_blue.copy(alpha = 0.15f),
                shape = RoundedCornerShape(10.dp)
            ).clip(RoundedCornerShape(10.dp))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Image(
                    painter = painterResource(Res.drawable.money_wings),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
                Text(
                    text = "Lorem ipsum dolor sit ametoridi massali consectetur massa.",
                    modifier = Modifier
                        .weight(1f) // Added weight modifier
                        .padding(horizontal = 16.dp), // Added horizontal padding
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                IconButton(
                    onClick = {},
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = null,
                        tint = primary_blue
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Transaksi Terbaru",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(uiState.latestTransactions) { transaction ->
                TransactionItem(transaction)
            }
        }
    }
}

@Composable
fun DebtCard(
    totalDebt: String,
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {
    val tabs = listOf("Utang", "Piutang")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .clip(RoundedCornerShape(10.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF5865F2),
                            Color(0xFF907EFD),
                            Color(0XFF926BFF)
                        )
                    )
                )
        )

        Image(
            painter = painterResource(Res.drawable.bg_card),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier.padding(18.dp).fillMaxSize(),
        ) {
            Row {
                Column {
                    Text(
                        text = "Total $selectedTab",
                        color = Color.White,
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = totalDebt,
                        color = Color.White,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    painter = painterResource(Res.drawable.chip),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .background(Color.White, shape = RoundedCornerShape(50))
                        .padding(vertical = 2.dp, horizontal = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    tabs.forEach { tab ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(if (tab == selectedTab) primary_blue else Color.Transparent)
                                .clickable { onTabSelected(tab) }
                                .padding(vertical = 8.dp, horizontal = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = tab,
                                color = if (tab == selectedTab) Color.White else grey2,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 6.dp)
                            )
                        }
                    }
                }
                Image(
                    painter = painterResource(Res.drawable.balikin_white),
                    contentDescription = "Balikin Logo",
                    alignment = Alignment.BottomCenter,
                    modifier = Modifier.size(64.dp)
                )
            }
        }
    }
}
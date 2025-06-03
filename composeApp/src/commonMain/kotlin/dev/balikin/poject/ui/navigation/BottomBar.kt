package dev.balikin.poject.ui.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import balikin.composeapp.generated.resources.Res
import balikin.composeapp.generated.resources.ic_history_fill
import balikin.composeapp.generated.resources.ic_history_outline
import balikin.composeapp.generated.resources.ic_home_fill
import balikin.composeapp.generated.resources.ic_home_outline
import balikin.composeapp.generated.resources.ic_user_fill
import balikin.composeapp.generated.resources.ic_user_outline
import balikin.composeapp.generated.resources.ic_wallet_fill
import balikin.composeapp.generated.resources.ic_wallet_outline
import dev.balikin.poject.ui.theme.grey2
import dev.balikin.poject.ui.theme.primary_blue
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

/**
 * Data class for bottom navigation items
 */
data class BottomNavItem(
    val title: String,
    val iconOutlined: DrawableResource,
    val iconFilled: DrawableResource,
    val screen: Screen
)

/**
 * Bottom navigation items list
 */
val navigationItems = listOf(
    BottomNavItem(
        title = "Home",
        iconOutlined = Res.drawable.ic_home_outline,
        iconFilled = Res.drawable.ic_home_fill,
        screen = Screen.Home
    ),
    BottomNavItem(
        title = "Transaksi",
        iconOutlined = Res.drawable.ic_wallet_outline,
        iconFilled = Res.drawable.ic_wallet_fill,
        screen = Screen.Transaction
    ),
    BottomNavItem(
        title = "History",
        iconOutlined = Res.drawable.ic_history_outline,
        iconFilled = Res.drawable.ic_history_fill,
        screen = Screen.History
    ),
    BottomNavItem(
        title = "Profile",
        iconOutlined = Res.drawable.ic_user_outline,
        iconFilled = Res.drawable.ic_user_fill,
        screen = Screen.Profile
    )
)

/**
 * Bottom navigation bar with centered FAB
 */
@Composable
fun BottomBarWithFab(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onFabClick: () -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Column(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant,
            thickness = 0.5.dp
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                navigationItems.forEachIndexed { index, item ->
                    if (index == 2) {
                        Spacer(Modifier.weight(1f))
                    }

                    BottomBarItem(
                        modifier = Modifier.weight(1f),
                        item = item,
                        selected = currentRoute == item.screen.route,
                        onClick = {
                            navController.navigate(item.screen.route) {
                                navController.graph.startDestinationRoute?.let {
                                    popUpTo(Screen.Home.route) {
                                        saveState = true
                                    }
                                    restoreState = true
                                    launchSingleTop = true
                                }
                            }
                        }
                    )
                }
            }

            FloatingActionButton(
                onClick = onFabClick,
                shape = CircleShape,
                containerColor = primary_blue,
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 0.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Add Transaction",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

/**
 * Individual item in bottom navigation bar
 *
 * @param item The navigation item to display
 * @param selected Whether this item is currently selected
 * @param onClick Action to perform when the item is clicked
 * @param modifier Optional modifier for the item
 */
@Composable
fun BottomBarItem(
    modifier: Modifier = Modifier,
    item: BottomNavItem,
    selected: Boolean,
    onClick: () -> Unit
) {
    val contentColor = if (selected) {
        primary_blue
    } else {
        grey2
    }

    val icon = if (selected) {
        item.iconFilled
    } else {
        item.iconOutlined
    }

    Box(
        modifier = modifier
            .height(64.dp)
            .clickable(
                onClick = onClick,
                onClickLabel = "Navigate to ${item.title}"  // Accessibility support
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = "${item.title} tab",  // Accessibility support
                tint = contentColor,
                modifier = Modifier.size(24.dp)
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = item.title,
                style = MaterialTheme.typography.labelSmall,
                color = contentColor
            )
        }
    }
}


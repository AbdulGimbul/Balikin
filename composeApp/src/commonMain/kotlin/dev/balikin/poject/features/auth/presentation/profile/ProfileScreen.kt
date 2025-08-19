package dev.balikin.poject.features.auth.presentation.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import balikin.composeapp.generated.resources.Res
import balikin.composeapp.generated.resources.agus
import balikin.composeapp.generated.resources.ic_edit
import balikin.composeapp.generated.resources.ic_profile_contact
import balikin.composeapp.generated.resources.ic_profile_email
import balikin.composeapp.generated.resources.ic_profile_faceid
import balikin.composeapp.generated.resources.ic_profile_logout
import balikin.composeapp.generated.resources.ic_profile_notif
import balikin.composeapp.generated.resources.ic_profile_user
import dev.balikin.poject.ui.components.AvatarImage
import dev.balikin.poject.ui.components.ProfileFriendItem
import dev.balikin.poject.ui.navigation.Screen
import dev.balikin.poject.ui.theme.primary_blue
import multiplatform.network.cmptoast.showToast
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ProfileScreen(viewModel: ProfileViewModel, navController: NavController) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.isLogout) {
        LaunchedEffect(Unit) {
            navController.navigate(Screen.Login.route) {
                popUpTo(0) {
                    inclusive = true
                }
            }
        }
    }

    uiState.errorMessage?.let {
        LaunchedEffect(it) {
            showToast(it)
        }
    }

    Profile(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        moveToFriends = { navController.navigate(Screen.Friends.route) }
    )
}

@Composable
fun Profile(
    uiState: ProfileUiState,
    onEvent: (ProfileUiEvent) -> Unit,
    moveToFriends: () -> Unit
) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var faceIDEnabled by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(contentAlignment = Alignment.BottomEnd) {
                uiState.userData?.name?.let {
                    AvatarImage(
                        name = it,
                        modifier = Modifier.clip(CircleShape),
                        size = 100.dp
                    )
                }
                Icon(
                    painter = painterResource(Res.drawable.ic_edit),
                    contentDescription = "Edit",
                    tint = primary_blue,
                    modifier = Modifier
                        .offset(x = (-8).dp, y = (-8).dp)
                        .size(24.dp)
                        .background(Color(0XFFE3E6FF), CircleShape)
                        .padding(4.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = uiState.userData?.name.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Information", color = Color.Gray, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF7F8F9), RoundedCornerShape(16.dp))
        ) {
            ProfileInfoCard(
                icon = Res.drawable.ic_profile_user,
                label = uiState.userData?.name.toString()
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 1.dp,
                color = Color.LightGray
            )
            ProfileInfoCard(
                icon = Res.drawable.ic_profile_email,
                label = uiState.userData?.email.toString()
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 1.dp,
                color = Color.LightGray
            )
            ProfileInfoCard(icon = Res.drawable.ic_profile_contact, label = "@agusihsan_")
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 1.dp,
                color = Color.LightGray
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Friends", color = Color.Gray, fontSize = 12.sp)
            TextButton(onClick = { }) {
                Text(
                    text = "See all",
                    color = primary_blue
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.friends.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF7F8F9), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(uiState.friends.take(4)) { friend ->
                        ProfileFriendItem(friend = friend)
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF7F8F9), RoundedCornerShape(16.dp))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No friends yet",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = { moveToFriends() },
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(1.dp, primary_blue),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = primary_blue,
                    containerColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(primary_blue.copy(alpha = 0.2f), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        modifier = Modifier.size(16.dp),
                        tint = primary_blue
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add friend", modifier = Modifier.padding(vertical = 6.dp))
            }

            Button(
                onClick = { onEvent(ProfileUiEvent.Logout) },
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = primary_blue,
                    containerColor = Color(0XFFFFEFEF)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(Res.drawable.ic_profile_logout),
                    contentDescription = "Logout",
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Logout", color = Color.Red, modifier = Modifier.padding(vertical = 6.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

    }
}

@Composable
fun ProfileInfoCard(icon: DrawableResource, label: String, isPassword: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Handle click */ }
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painter = painterResource(icon), contentDescription = null)
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
            fontSize = 14.sp
        )
        Icon(
            Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.Gray
        )
    }
}

@Composable
fun PreferenceSwitch(label: String, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = if (label == "Notifications") painterResource(Res.drawable.ic_profile_notif) else painterResource(
                Res.drawable.ic_profile_faceid
            ),
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = label, modifier = Modifier.weight(1f))
        Switch(checked = isChecked, onCheckedChange = onCheckedChange)
    }
}

@Composable
@Preview
fun ProfilePreview() {
    Profile(uiState = ProfileUiState(), onEvent = {}, moveToFriends = {})
}
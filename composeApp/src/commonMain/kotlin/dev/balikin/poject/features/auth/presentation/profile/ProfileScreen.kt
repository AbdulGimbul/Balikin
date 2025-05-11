package dev.balikin.poject.features.auth.presentation.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import balikin.composeapp.generated.resources.Res
import balikin.composeapp.generated.resources.agus
import org.jetbrains.compose.resources.painterResource

@Composable
fun ProfileScreen() {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var faceIDEnabled by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Profile Picture
        Box(contentAlignment = Alignment.BottomEnd) {
            Image(
                painter = painterResource(Res.drawable.agus),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit",
                tint = Color(0xFF8875FF),
                modifier = Modifier
                    .offset(x = (-8).dp, y = (-8).dp)
                    .size(24.dp)
                    .background(Color.White, CircleShape)
                    .padding(4.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Agus Ihsan Mochamad",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Information Section
        Text("Information", color = Color.Gray, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF7F8F9), RoundedCornerShape(16.dp))
        ) {
            ProfileInfoCard(icon = Icons.Outlined.Person, label = "Agus Ihsan Mochamad")
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 1.dp,
                color = Color.LightGray
            )
            ProfileInfoCard(icon = Icons.Outlined.Email, label = "agusikhsan08@gmail.com")
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 1.dp,
                color = Color.LightGray
            )
            ProfileInfoCard(icon = Icons.Outlined.AccountBox, label = "@agusihsan_")
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 1.dp,
                color = Color.LightGray
            )
            ProfileInfoCard(icon = Icons.Outlined.Lock, label = "************", isPassword = true)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("PREFERENCES", color = Color.Gray, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF7F8F9), RoundedCornerShape(16.dp))
        ) {
            PreferenceSwitch(label = "Notifications", isChecked = notificationsEnabled) {
                notificationsEnabled = it
            }
            HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
            PreferenceSwitch(label = "Face ID", isChecked = faceIDEnabled) {
                faceIDEnabled = it
            }
            HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* Handle logout */ }
                    .padding(vertical = 16.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = "Logout",
                    tint = Color.Red
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text("Logout", color = Color.Red, fontWeight = FontWeight.Medium)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


    }
}

@Composable
fun ProfileInfoCard(icon: ImageVector, label: String, isPassword: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .clickable { /* Handle click */ },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = Color.Gray)
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
            fontSize = 14.sp
        )
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.Gray)
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
        Icon(
            imageVector = if (label == "Notifications") Icons.Outlined.Notifications else Icons.Outlined.Face,
            contentDescription = null,
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = label, modifier = Modifier.weight(1f))
        Switch(checked = isChecked, onCheckedChange = onCheckedChange)
    }
}
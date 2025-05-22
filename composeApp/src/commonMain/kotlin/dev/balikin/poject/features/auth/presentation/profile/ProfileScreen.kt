package dev.balikin.poject.features.auth.presentation.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import balikin.composeapp.generated.resources.Res
import balikin.composeapp.generated.resources.agus
import balikin.composeapp.generated.resources.ic_edit
import balikin.composeapp.generated.resources.ic_profile_contact
import balikin.composeapp.generated.resources.ic_profile_email
import balikin.composeapp.generated.resources.ic_profile_faceid
import balikin.composeapp.generated.resources.ic_profile_lock
import balikin.composeapp.generated.resources.ic_profile_logout
import balikin.composeapp.generated.resources.ic_profile_notif
import balikin.composeapp.generated.resources.ic_profile_user
import dev.balikin.poject.ui.theme.primary_blue
import org.jetbrains.compose.resources.DrawableResource
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
            ProfileInfoCard(icon = Res.drawable.ic_profile_user, label = "Agus Ihsan Mochamad")
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 1.dp,
                color = Color.LightGray
            )
            ProfileInfoCard(icon = Res.drawable.ic_profile_email, label = "agusikhsan08@gmail.com")
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
            ProfileInfoCard(
                icon = Res.drawable.ic_profile_lock,
                label = "************",
                isPassword = true
            )
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
                Image(
                    painter = painterResource(Res.drawable.ic_profile_logout),
                    contentDescription = "Logout",
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text("Logout", color = Color.Red, fontWeight = FontWeight.Medium)
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
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .clickable { /* Handle click */ },
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
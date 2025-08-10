package dev.balikin.poject.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import balikin.composeapp.generated.resources.Res
import balikin.composeapp.generated.resources.agus
import dev.balikin.poject.features.friends.domain.FollowedUser
import org.jetbrains.compose.resources.painterResource

@Composable
fun ProfileFriendItem(
    friend: FollowedUser,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (friend.isOnline) {
            Image(
                painter = painterResource(Res.drawable.agus),
                contentDescription = "User Avatar",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
            )
        } else {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Offline Indicator",
                modifier = Modifier
                    .size(60.dp)
                    .background(Color.LightGray, CircleShape)
                    .clip(CircleShape),
                tint = Color.Gray
            )
        }

        Text(
            text = friend.name,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.size(width = 70.dp, height = 16.dp)
        )
    }
}

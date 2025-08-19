package dev.balikin.poject.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
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
import dev.balikin.poject.features.friends.domain.FollowedUser

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
            AvatarImage(
                name = friend.name,
                modifier = Modifier.clip(CircleShape),
                size = 48.dp
            )
        } else {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Offline Indicator",
                modifier = Modifier
                    .size(48.dp)
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
            modifier = Modifier.width(70.dp)
        )
    }
}

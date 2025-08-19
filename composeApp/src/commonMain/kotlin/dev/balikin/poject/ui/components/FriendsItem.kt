package dev.balikin.poject.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import balikin.composeapp.generated.resources.Res
import balikin.composeapp.generated.resources.agus
import dev.balikin.poject.ui.theme.green
import org.jetbrains.compose.resources.painterResource

import dev.balikin.poject.features.friends.domain.FriendApiModel
import dev.balikin.poject.features.friends.domain.FriendsApiData

@Composable
fun FriendsItem(
    friend: FriendsApiData,
    modifier: Modifier = Modifier,
    onAddFriend: (String) -> Unit = {},
    isAddingFriend: Boolean = false,
    addingFriendEmail: String? = null,
    isAlreadyFriend: Boolean = false
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AvatarImage(
                name = friend.name,
                modifier = Modifier.clip(CircleShape),
                size = 40.dp
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = friend.name,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                Text(
                    text = friend.email,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            
            // Only show add button if not already friends
            if (!isAlreadyFriend) {
                val isThisFriendLoading = isAddingFriend && addingFriendEmail == friend.email
                
                if (isThisFriendLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = green,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Add",
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { onAddFriend(friend.email) }
                            .background(
                                color = green,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        color = Color.White,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider()
    }
}

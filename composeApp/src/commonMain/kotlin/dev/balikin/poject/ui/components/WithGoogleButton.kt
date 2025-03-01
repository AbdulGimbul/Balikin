package dev.balikin.poject.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import balikin.composeapp.generated.resources.Res
import balikin.composeapp.generated.resources.icons_google
import dev.balikin.poject.ui.theme.grey
import dev.balikin.poject.ui.theme.primary_text
import org.jetbrains.compose.resources.painterResource

@Composable
fun WithGoogleButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonText: String = "Sign up with Google"
) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, grey),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(Res.drawable.icons_google),
                contentDescription = "Google logo",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = buttonText,
                color = primary_text,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}
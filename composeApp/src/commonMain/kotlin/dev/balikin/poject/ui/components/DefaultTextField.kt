package dev.balikin.poject.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import dev.balikin.poject.ui.theme.bg_light
import dev.balikin.poject.ui.theme.secondary_text

@Composable
fun DefaultTextField(
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    label: String? = null,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    minLines: Int = 1,
    singleLine: Boolean = true,
    placehoder: String? = null,
    keyboardOptions: KeyboardOptions? = null
) {
    var passwordVisibility by remember { mutableStateOf(isPassword) }

    TextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = MaterialTheme.typography.bodyMedium,
        label = if (label != null) {
            {
                Text(text = label, color = secondary_text)
            }
        } else null,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = bg_light,
            unfocusedContainerColor = bg_light,
            cursorColor = secondary_text,
            focusedLabelColor = secondary_text,
            unfocusedLabelColor = secondary_text,
            focusedPlaceholderColor = secondary_text,
            unfocusedPlaceholderColor = secondary_text,
            focusedLeadingIconColor = secondary_text,
            unfocusedLeadingIconColor = secondary_text,
            focusedTrailingIconColor = secondary_text,
            unfocusedTrailingIconColor = secondary_text,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        leadingIcon = if (leadingIcon != null) {
            {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = label
                )
            }
        } else null,
        trailingIcon = if (isPassword) {
            {
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(
                        imageVector = if (isPassword && passwordVisibility) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                        contentDescription = "Password"
                    )
                }
            }
        } else null,
        visualTransformation = if (isPassword && passwordVisibility) PasswordVisualTransformation() else VisualTransformation.None,
        minLines = minLines,
        singleLine = singleLine,
        placeholder = if (placehoder != null) {
            {
                Text(text = placehoder, color = secondary_text)
            }
        } else null,
        keyboardOptions = keyboardOptions ?: KeyboardOptions.Default
    )
}
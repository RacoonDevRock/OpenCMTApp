package com.cmt.openapp.core.ui.shared.buttonNavigate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun MyButton(
    navigate: () -> Unit,
    textButton: String,
    myIconButton: ImageVector,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = {
            navigate()
        },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onSecondary
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = textButton,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary
            )
            Icon(
                myIconButton,
                contentDescription = "navigate",
                tint = MaterialTheme.colorScheme.secondary
            )
        }
    }
}
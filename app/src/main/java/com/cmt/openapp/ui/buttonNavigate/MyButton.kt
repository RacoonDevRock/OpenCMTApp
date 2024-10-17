package com.cmt.openapp.ui.buttonNavigate

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.sp

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
            containerColor = MaterialTheme.colorScheme.tertiary
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = textButton, fontSize = 21.sp, color = Color.White)
            Icon(myIconButton, contentDescription = "navigate", tint = Color.White)
        }
    }
}
package com.cmt.openapp.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmt.openapp.R

//@Preview(showSystemUi = true)
@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE5E5E5))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.open_logo),
                contentDescription = "logo_cmt",
                Modifier
                    .size(300.dp)
                    .align(Alignment.BottomCenter)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(topStart = 130.dp, topEnd = 130.dp))
                .background(Color(0xFFD9D9D9)),
        ) {
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("Conoce sobre los ")
                        withStyle(style = SpanStyle(color = Color.Black)) {
                            append("incidentes\n") // Aquí agregas el salto de línea
                        }
                        append("y solicita informes detallados")
                    },
                    textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(40.dp))
                Button(onClick = { }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF848688))) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Comenzar", fontSize = 21.sp)
                        Icon(Icons.Default.PlayArrow, contentDescription = "navigate")
                    }
                }
            }
        }
    }
}
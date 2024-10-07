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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.cmt.openapp.R
import com.cmt.openapp.model.Routes

//@Preview(showSystemUi = true)
@Composable
fun HomeScreen(modifier: Modifier, navigationController: NavHostController) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LogoSection(Modifier.weight(1f))
        InfoSection(Modifier.weight(1f)) { navigationController.navigate(Routes.ResearchScreen.route) }
    }
}

@Composable
fun LogoSection(modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.open_logo),
            contentDescription = "logo_cmt",
            Modifier
                .size(300.dp)
                .aspectRatio(1f)
                .align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun InfoSection(modifier: Modifier, navigate: () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 130.dp, topEnd = 130.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
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
                        append("incidentes\n")
                    }
                    append("y solicita informes detallados")
                },
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(40.dp))
            MyButtonNavigate(navigate, "Comenzar", Icons.Default.PlayArrow)
        }
    }
}

@Composable
fun MyButtonNavigate(navigate: () -> Unit, textButton: String, myIconButton: ImageVector) {
    Button(
        onClick = {
            navigate()
        },
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
    ) {
        Row() {
            Text(text = textButton, fontSize = 21.sp, color = Color.White)
            Icon(myIconButton, contentDescription = "navigate", tint = Color.White)
        }
    }
}

package com.cmt.openapp.ui.home

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.cmt.openapp.R
import com.cmt.openapp.model.Routes

//@Preview(showSystemUi = true)
@Composable
fun HomeScreen(modifier: Modifier, navigationController: NavHostController) {
    val navigateToResearch = remember { Routes.ResearchScreen.route }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LogoSection(Modifier.weight(1f))
        InfoSection(Modifier.weight(1f)) { navigationController.navigate(navigateToResearch) }
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
            .clip(RoundedCornerShape(topStart = 110.dp, topEnd = 110.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.home_description),
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                lineHeight = 20.sp,
                modifier = Modifier.padding(horizontal = 50.dp)
            )
            Spacer(modifier = Modifier.height(40.dp))
            MyButtonNavigate(
                navigate,
                stringResource(id = R.string.home_button),
                Icons.Default.PlayArrow
            )
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

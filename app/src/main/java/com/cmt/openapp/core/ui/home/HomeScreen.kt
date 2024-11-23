package com.cmt.openapp.core.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cmt.openapp.R
import com.cmt.openapp.core.navigation.Routes
import com.cmt.openapp.core.ui.shared.buttonNavigate.MyButton
import com.cmt.openapp.core.ui.shared.dialog.CustomDialogDiagnostic
import com.cmt.openapp.core.ui.shared.dialog.FormDiagnostic

@Composable
fun HomeScreen(
    modifier: Modifier,
    navigationController: NavHostController,
    onThemeChange: (Int) -> Unit = {},
    onTypographyChange: (Typography) -> Unit,
    onFirstLaunchComplete: () -> Unit
) {
    val navigateToResearch = remember { Routes.ResearchScreen.route }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LogoSection(Modifier.weight(1f))
        InfoSection(Modifier.weight(1f), onNavigate = {
            onFirstLaunchComplete()
            navigationController.navigate(
                navigateToResearch
            )
        }, onThemeChange = onThemeChange, onTypographyChange = onTypographyChange)
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
                .align(Alignment.BottomCenter),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun InfoSection(
    modifier: Modifier,
    onNavigate: () -> Unit,
    onThemeChange: (Int) -> Unit = {},
    onTypographyChange: (Typography) -> Unit,
) {
    var showDiagnostic by remember { mutableStateOf(false) }

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
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 50.dp)
            )
            Spacer(modifier = Modifier.height(40.dp))

            if (showDiagnostic) {
                CustomDialogDiagnostic {
                    FormDiagnostic(
                        { responses ->
                            responses.forEach { (question, answer) ->
                                println("Pregunta $question, Respuesta: $answer")
                            }
                        },
                        onThemeChange = onThemeChange,
                        onTypographyChange = onTypographyChange,
                        onNavigateToResearch = onNavigate
                    )
                }
            }

            MyButton(
                { showDiagnostic = true },
                stringResource(id = R.string.home_button),
                Icons.Default.PlayArrow
            )
        }
    }
}

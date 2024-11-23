package com.cmt.openapp.core.ui.shared.dialog

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.cmt.openapp.R
import com.cmt.openapp.ui.theme.LargeTypography
import com.cmt.openapp.ui.theme.MediumTypography
import com.cmt.openapp.ui.theme.NormalTypography

@Composable
fun TopDialogSheet(onDismissRequest: () -> Unit, content: @Composable () -> Unit) {
    Dialog(onDismissRequest = onDismissRequest) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(20.dp))
                .padding(20.dp)
        ) {
            content()
        }
    }
}

@Composable
fun MyConfirmationReport(
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
) {
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(text = "Confirmación", style = MaterialTheme.typography.titleSmall)
        Text(
            "¿Seguro que quieres salir? Tus datos no serán guardados.",
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            ButtonConfirmReport(onClick = onConfirm, "Sí, descartar")
            ButtonConfirmReport(onClick = onCancel, "Cancelar")
        }
    }
}

@Composable
fun ButtonConfirmReport(onClick: () -> Unit, textButton: String) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onSecondary
        )
    ) {
        Text(
            text = textButton,
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
fun InfoContent() {
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(id = R.string.header_info_app),
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(id = R.string.n_law_info_app),
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = stringResource(id = R.string.body_info_app),
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = stringResource(id = R.string.footer_info_app),
            color = MaterialTheme.colorScheme.tertiary,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(horizontal = 5.dp)
        )
    }
}

@Composable
fun CustomDialogDiagnostic(content: @Composable () -> Unit) {
    Dialog(onDismissRequest = { }) {
        Box(
            modifier = Modifier
                .width(300.dp)
                .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(20.dp))
                .padding(20.dp),

            ) {
            content()
        }
    }
}

@Composable
fun FormDiagnostic(
    onSubmit: (Map<String, String>) -> Unit,
    onThemeChange: (Int) -> Unit = {},
    onTypographyChange: (Typography) -> Unit,
    onNavigateToResearch: () -> Unit,
) {
    var answer1 by remember { mutableStateOf<String?>(null) }
    var answer2 by remember { mutableStateOf<String?>(null) }
    var answer3 by remember { mutableStateOf<String?>(null) }

    val question1 = stringResource(id = R.string.question_fatigue)
    val question2 = stringResource(id = R.string.question_astigmatism)
    val question3 = stringResource(id = R.string.question_reading_difficulty)

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.diagnostic_form_title),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
        )

        DiagnosticQuestion(
            question = question1,
            selectedAnswer = answer1,
            onAnswerSelected = { answer1 = it }
        )

        DiagnosticQuestion(
            question = question2,
            selectedAnswer = answer2,
            onAnswerSelected = { answer2 = it }
        )

        DiagnosticQuestion(
            question = question3,
            selectedAnswer = answer3,
            onAnswerSelected = { answer3 = it }
        )

        Button(
            onClick = {
                val responses = mapOf(
                    question1 to (answer1 ?: ""),
                    question2 to (answer2 ?: ""),
                    question3 to (answer3 ?: "")
                )

                val typography = when {
                    answer2 == "Sí" && answer3 == "Sí" -> LargeTypography
                    answer2 == "Sí" || answer3 == "Sí" -> MediumTypography
                    else -> NormalTypography
                }
                onTypographyChange(typography)

                val isDarkTheme = answer1 == "Sí"
                onThemeChange(if (isDarkTheme) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)

                onSubmit(responses)

                onNavigateToResearch()
            },
            enabled = answer1 != null && answer2 != null && answer3 != null,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = stringResource(id = R.string.ready))
        }
    }
}

@Composable
fun DiagnosticQuestion(
    question: String,
    selectedAnswer: String?,
    onAnswerSelected: (String) -> Unit,
) {
    Column(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = question,
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = selectedAnswer == "Sí",
                onClick = { onAnswerSelected("Sí") }
            )
            Text(
                text = "Sí",
                style = MaterialTheme.typography.displaySmall
            )
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                selected = selectedAnswer == "No",
                onClick = { onAnswerSelected("No") }
            )
            Text(
                text = "No",
                style = MaterialTheme.typography.displaySmall
            )
        }
    }
}

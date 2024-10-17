package com.cmt.openapp.ui.incident

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilePresent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.cmt.openapp.R
import com.cmt.openapp.model.Routes
import com.cmt.openapp.ui.buttonNavigate.MyButton
import com.cmt.openapp.ui.dialog.InfoContent
import com.cmt.openapp.ui.dialog.TopDialogSheet
import com.cmt.openapp.ui.research.HeaderSection

@Composable
fun DetailIncidentScreen(modifier: Modifier, navigationController: NavHostController) {
    var isTopDialogVisible by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            HeaderSection { isTopDialogVisible = true }

            Spacer(modifier = Modifier.height(20.dp))

            IncidentDetailsContainer(Modifier.weight(1f))

            Spacer(modifier = Modifier.height(20.dp))

            RequestedBox {
                navigationController.navigate(Routes.ReportScreen.route)
            }

        }

        if (isTopDialogVisible) {
            TopDialogSheet(onDismissRequest = { isTopDialogVisible = false }) {
                InfoContent()
            }
        }
    }
}

@Composable
fun IncidentDetailsContainer(modifier: Modifier) {
    Box(
        modifier = modifier
            .padding(horizontal = 25.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        val scrollState = rememberScrollState()

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            IncidentHeader("1999", "03/08/2024 10:29")
            Spacer(modifier = Modifier.height(15.dp))
            IncidentDetails(
                "Consumo de licor en la vía pública",
                "Alambre",
                "Cortijo",
                "Persuasiva",
                "Positivo",
                "Contribuyentes refieren que en el lugar se encuentran un grupo de 20 personas ingiriendo bebidas alcohólicas."
            )
        }
    }
}

@Composable
fun RequestedBox(navigate: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp)
            .clip(RoundedCornerShape(topStart = 110.dp, topEnd = 110.dp))
            .background(MaterialTheme.colorScheme.primaryContainer),
    ) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.previous_info_report),
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                lineHeight = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(horizontal = 50.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(25.dp))
            MyButton(
                navigate,
                stringResource(id = R.string.previous_button_report),
                Icons.Default.FilePresent
            )
        }
    }
}

@Composable
fun IncidentDetails(
    incidentType: String,
    zone: String,
    sector: String,
    interventionType: String,
    interventionResult: String,
    observations: String,
) {
    MySection(stringResource(id = R.string.incident_type_field_filter))
    MySectionData(incidentType)
    Spacer(modifier = Modifier.height(3.dp))
    MySection(stringResource(id = R.string.zone_field_filter))
    MySectionData(zone)
    Spacer(modifier = Modifier.height(3.dp))
    MySection(stringResource(id = R.string.sector_field_filter))
    MySectionData(sector)
    Spacer(modifier = Modifier.height(3.dp))
    MySection(stringResource(id = R.string.subtitle_intervention_type_report))
    MySectionData(interventionType)
    Spacer(modifier = Modifier.height(3.dp))
    MySection(stringResource(id = R.string.subtitle_intervention_result_report))
    MySectionData(interventionResult)
    Spacer(modifier = Modifier.height(3.dp))
    MySection(stringResource(id = R.string.subtitle_observers_button))
    MySectionData(observations)
}

@Composable
fun IncidentHeader(incidentNumber: String, date: String) {
    Row(
        Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Incidente N° $incidentNumber",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = date,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Color.Black
        )
    }
}

@Composable
fun MySectionData(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.ExtraBold,
        textAlign = TextAlign.Justify,
        color = MaterialTheme.colorScheme.primary,
        lineHeight = 16.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 5.dp)
    )
}

@Composable
fun MySection(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.tertiary,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 12.sp,
        modifier = Modifier.fillMaxWidth()
    )
}


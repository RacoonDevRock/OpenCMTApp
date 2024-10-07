package com.cmt.openapp.screen

import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilePresent
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.cmt.openapp.model.Routes

//@Preview(showSystemUi = true, device = "spec:width=412dp,height=915dp,dpi=420")
@Composable
fun DetailIncidentScreen(modifier: Modifier, navigationController: NavHostController) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            HeaderSection()

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 25.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    IncidentHeader("1999", "03/08/2024 10:29")
                    Spacer(modifier = Modifier.height(10.dp))
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

            Spacer(modifier = Modifier.height(20.dp))

            RequestedBox {
                navigationController.navigate(Routes.RequestScreen.route)
            }

        }
    }
}

@Composable
fun RequestedBox(navigate: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp)
            .clip(RoundedCornerShape(topStart = 130.dp, topEnd = 130.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .animateContentSize(),
    ) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                buildAnnotatedString {
                    append("Si desea obtener un informe ")
                    withStyle(style = SpanStyle(color = Color.Black)) {
                        append("completo\n") // Aquí agregas el salto de línea
                    }
                    append("del incidente, debe solicitarlo.")
                },
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .align(Alignment.CenterHorizontally)
            )
            MyButtonNavigate(navigate, "Solicitar", Icons.Default.FilePresent)
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
    MySection("Tipo de Incidente")
    MySectionData(incidentType)
    Spacer(modifier = Modifier.height(3.dp))
    MySection("Zona")
    MySectionData(zone)
    Spacer(modifier = Modifier.height(3.dp))
    MySection("Sector")
    MySectionData(sector)
    Spacer(modifier = Modifier.height(3.dp))
    MySection("Tipo de Intervención")
    MySectionData(interventionType)
    Spacer(modifier = Modifier.height(3.dp))
    MySection("Resultado de la Intervencion")
    MySectionData(interventionResult)
    Spacer(modifier = Modifier.height(3.dp))
    MySection("Observaciones")
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
        color = Color.Black,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun MySection(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.tertiary,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.fillMaxWidth()
    )
}


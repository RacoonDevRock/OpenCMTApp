package com.cmt.openapp.screen

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Info
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
fun DetailIncidentScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE5E5E5))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "information",
                    Modifier.padding(18.dp), tint = Color(0xFF848688)
                )
                Image(
                    painter = painterResource(id = R.drawable.open_logo_small),
                    contentDescription = "logo_small",
                    Modifier
                        .padding(top = 30.dp)
                        .align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 25.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(14.dp)
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Incidente N° 1999",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "03/08/2024 10:29",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Column(horizontalAlignment = Alignment.Start) {
                        MySection("Tipo de Incidente")
                        MySectionData("Consumo de licor en la vía pública")
                        Spacer(modifier = Modifier.height(3.dp))
                        MySection("Zona")
                        MySectionData("Alambre")
                        Spacer(modifier = Modifier.height(3.dp))
                        MySection("Sector")
                        MySectionData("Cortijo")
                        Spacer(modifier = Modifier.height(3.dp))
                        MySection("Tipo de Intervención")
                        MySectionData("Persuasiva")
                        Spacer(modifier = Modifier.height(3.dp))
                        MySection("Resultado de la Intervencion")
                        MySectionData("Positivo")
                        Spacer(modifier = Modifier.height(3.dp))
                        MySection("Observaciones")
                        MySectionData("Contribuyentes refieren que en el lugar se encuentran un grupo de 20 personas ingiriendo bebidas alcohólicas.")
                    }
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            // Box anclado al fondo, que se expande/contrae con click
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 130.dp, topEnd = 130.dp))
                    .background(Color(0xFFD9D9D9))
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .height(210.dp),
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
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(bottom = 24.dp)
                            .align(Alignment.CenterHorizontally)
                    )

                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF848688))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Solicitar", fontSize = 21.sp)
                            Icon(Icons.Default.FilePresent, contentDescription = "request")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MySectionData(text: String) {
    Text(
        text = text,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 6.dp)
    )
}

@Composable
fun MySection(text: String) {
    Text(
        text = text,
        color = Color(0xFF848688),
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 3.dp)
    )
}


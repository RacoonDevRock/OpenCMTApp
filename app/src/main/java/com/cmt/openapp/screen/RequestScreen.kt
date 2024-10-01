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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilePresent
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
fun RequestScreen() {
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

            Spacer(modifier = Modifier.height(20.dp))

            // Box anclado al fondo, que se expande/contrae con click
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(topStart = 130.dp, topEnd = 130.dp))
                    .background(Color(0xFFD9D9D9))
            ) {
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = buildAnnotatedString {
                            append("Solicitud ")
                            withStyle(style = SpanStyle(color = Color.Black)) {
                                append("de\n") // Aquí agregas el salto de línea
                            }
                            append("Acceso a la Información")
                        },
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(vertical = 24.dp)
                            .align(Alignment.CenterHorizontally)
                    )

                    Text(
                        text = "Incidente N°1999",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(bottom = 20.dp)
                            .align(Alignment.CenterHorizontally)
                    )

                    TextFieldRequest("Nombre Completo/Razón Social")
                    TextFieldRequest("DNI/CE/RUC/Otros")
                    TextFieldRequest("Domicilio")
                    TextFieldRequest("Distrito")
                    TextFieldRequest("Correo Electrónico")
                    TextFieldRequest("Teléfono/Celular")
                    TextFieldRequest("Motivo")

                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF848688)),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
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
fun TextFieldRequest(text: String) {
    TextField(
        value = "",
        onValueChange = { },
        placeholder = {
            Text(
                text = text,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(start = 4.dp),
                fontSize = 14.sp,
                color = Color(0xFF848688)
            )
        },
        readOnly = true,
        modifier = Modifier
            .padding(bottom = 13.dp)
            .width(310.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            unfocusedTextColor = Color(0XFF848688),
            unfocusedTrailingIconColor = Color(0XFF848688),
            focusedTrailingIconColor = Color(0XFF848688),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(25.dp)
    )
}

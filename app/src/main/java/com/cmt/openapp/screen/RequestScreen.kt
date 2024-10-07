package com.cmt.openapp.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilePresent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.cmt.openapp.model.Routes

//@Preview(showSystemUi = true)
@Composable
fun RequestScreen(modifier: Modifier, navigationController: NavHostController) {
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

            BoxRequest(Modifier.weight(1f)) { navigationController.navigate(Routes.HomeScreen.route) }


        }
    }
}

@Composable
fun BoxRequest(modifier: Modifier, navigate: () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 130.dp, topEnd = 130.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            RequestHeader(Modifier.align(Alignment.CenterHorizontally))
            RequestForm()
            MyButtonNavigate(navigate, "Solicitar", Icons.Default.FilePresent)
        }
    }
}

@Composable
fun RequestForm() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        TextFieldRequest("Nombre Completo/Razón Social", "") {}
        TextFieldRequest("DNI/CE/RUC/Otros", "") {}
        TextFieldRequest("Domicilio", "") {}
        TextFieldRequest("Distrito", "") {}
        TextFieldRequest("Correo Electrónico", "") {}
        TextFieldRequest("Teléfono/Celular", "") {}
        TextFieldRequest("Motivo", "") {}
    }
}

@Composable
fun RequestHeader(modifier: Modifier) {
    Text(
        text = buildAnnotatedString {
            append("Solicitud de")
            withStyle(style = SpanStyle(color = Color.Black)) {
                append("Acceso\n") // Aquí agregas el salto de línea
            }
            append("a la Información")
        },
        color = Color.Black,
        textAlign = TextAlign.Center,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
            .padding(top = 24.dp, bottom = 15.dp)
    )

    Text(
        text = "Incidente N°1999",
        color = Color.Black,
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .padding(bottom = 15.dp)
    )
}

@Composable
fun TextFieldRequest(label: String, value: String, onValueChange: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = label,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(start = 4.dp),
                fontSize = 13.sp,
                color = Color(0xFF848688)
            )
        },
        modifier = Modifier
            .padding(bottom = 13.dp)
            .width(290.dp)
            .height(50.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
            unfocusedTrailingIconColor = MaterialTheme.colorScheme.tertiary,
            focusedTrailingIconColor = MaterialTheme.colorScheme.tertiary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(25.dp)
    )
}

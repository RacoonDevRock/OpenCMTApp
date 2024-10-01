package com.cmt.openapp.screen

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmt.openapp.R
import java.util.Calendar

//@Preview(showSystemUi = true)
@Composable
fun ResearchScreen() {
    var isExpanded by remember { mutableStateOf(false) }

    val maxHeight = 500.dp // Altura máxima
    val minHeight = 100.dp // Altura mínima

    // Animación de la altura del Box
    val animatedBoxHeight by animateDpAsState(
        targetValue = if (isExpanded) maxHeight else minHeight,
        animationSpec = tween(durationMillis = 800)
    )

    // Animación para rotar el ícono
    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = tween(durationMillis = 800)
    )

    val context = LocalContext.current
    var date by remember { mutableStateOf("") }

    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            date = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    var zone by remember { mutableStateOf("") }
    var sector by remember { mutableStateOf("") }
    var accidentType by remember { mutableStateOf("") }

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

            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .weight(1f), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(20) {
                    IncidentBox()
                }
            }

            Spacer(modifier = Modifier.height(80.dp))
        }

        // Box anclado al fondo, que se expande/contrae con click
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(animatedBoxHeight)
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(topStart = 130.dp, topEnd = 130.dp))
                .background(Color(0xFFD9D9D9))
                .clickable {
                    isExpanded = !isExpanded // Cambia el estado
                }
        ) {
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Icono rotativo
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowUp,
                    contentDescription = "",
                    modifier = Modifier
                        .padding(20.dp)
                        .rotate(rotationAngle), // Rotamos el ícono con la animación
                    tint = Color(0XFF848688)
                )
                Text(
                    text = "Filtros",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 20.dp)
                )
                MyTextField(date, { date = it }, "Fecha") {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "", Modifier.clickable { datePickerDialog.show() }
                    )
                }
                MyTextField(zone, { zone = it }, "Zona") {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "", Modifier.clickable { datePickerDialog.show() }
                    )
                }
                MyTextField(sector, { sector = it }, "Sector") {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "", Modifier.clickable { datePickerDialog.show() }
                    )
                }
                MyTextField(accidentType, { accidentType = it }, "Tipo de Incidente") {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "", Modifier.clickable { datePickerDialog.show() }
                    )
                }

                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF848688))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Buscar", fontSize = 21.sp)
                        Icon(Icons.Default.Search, contentDescription = "search")
                    }
                }
            }
        }
    }
}

@Composable
fun IncidentBox() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(start = 26.dp, end = 26.dp, bottom = 16.dp)
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
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "03/08/2024 10:29",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Consumo de licor en la vía pública",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun MyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    trailingIcon: @Composable () -> Unit
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(start = 8.dp),
                color = Color(0xFF848688)
            )
        },
        readOnly = true,
        modifier = Modifier
            .padding(bottom = 25.dp)
            .width(310.dp),
        trailingIcon = trailingIcon,
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